package kyta.composter.protocol.packet

import kyta.composter.item.*
import kyta.composter.network.Connection
import kyta.composter.protocol.ConnectionState
import kyta.composter.protocol.Global
import kyta.composter.protocol.PacketHandler
import kyta.composter.protocol.packet.handshaking.C2SHandshakePacket
import kyta.composter.protocol.packet.handshaking.S2CHandshakePacket
import kyta.composter.protocol.packet.login.C2SLoginPacket
import kyta.composter.protocol.packet.login.S2CLoginPacket
import kyta.composter.protocol.packet.ping.PingPacket
import kyta.composter.protocol.packet.play.*
import kyta.composter.server.MinecraftServer
import kyta.composter.server.withContext
import kyta.composter.world.BlockPos
import kyta.composter.world.ChunkPos
import kyta.composter.world.block.Block
import kyta.composter.world.block.BlockState
import kyta.composter.world.block.STONE
import kyta.composter.world.block.isAir
import kyta.composter.world.breakBlock
import kyta.composter.world.entity.*
import kyta.composter.world.getCollidingEntities
import net.kyori.adventure.text.Component

class DefaultPacketHandler(
    private val server: MinecraftServer,
    private val connection: Connection,
) : PacketHandler {
    private lateinit var player: Player
    private val logger = server.logger

    override suspend fun handleHandshake(packet: C2SHandshakePacket) {
        connection.sendPacket(S2CHandshakePacket("-"))
        connection.state = ConnectionState.LOGIN
    }

    override suspend fun handleKeepAlive(packet: GenericKeepAlivePacket) {
    }

    override suspend fun handleLogin(packet: C2SLoginPacket) {
        if (connection.state != ConnectionState.LOGIN) {
            return connection.disconnect("Invalid connection state (${connection.state.name})")
        }

        if (packet.protocolVersion != 14) {
            return connection.disconnect("Unsupported protocol version (${packet.protocolVersion})")
        }

        /* disconnect existing players with the same username */
        withContext(server) {
            server.playerList.getPlayer(packet.username)
                ?.connection
                ?.disconnect("You logged in from another location")

            player = Player(connection, packet.username)
            player.world = server.worldManager.primaryWorld

            /* acknowledge login, advance connection status */
            connection.sendPacket(
                S2CLoginPacket(
                    player.id,
                    "composter",
                    player.world.properties.seed,
                    player.world.properties.dimensionType,
                )
            ).sync()
            connection.player = player
            connection.state = ConnectionState.PLAY

            /* finish up the rest of the joining flow */
            server.playerList.playerJoined(player)
        }
    }

    override suspend fun handleChatMessage(packet: C2SChatMessagePacket) {
        if (packet.message.isBlank()) return
        connection.player.inventory.insert(ItemStack(Item(STONE.networkId), 64, 0))
        server.playerList.broadcastMessage(Component.text("<${connection.player.username}> ${packet.message}"))
    }

    override suspend fun handleEntityAction(packet: C2SEntityActionPacket) = withContext(server) {
        when (packet.action) {
            C2SEntityActionPacket.Action.START_CROUCHING -> player.crouching = true
            C2SEntityActionPacket.Action.STOP_CROUCHING -> player.crouching = false

            else -> throw UnsupportedOperationException()
        }
    }

    override suspend fun handlePlayerFlyingStatus(packet: FlyingStatusPacket) {
        withContext(server) {
            connection.player.isOnGround = packet.onGround
        }
    }

    override suspend fun handlePlayerPosition(packet: PositionPacket) {
        val currentPos = connection.player.pos

        if (currentPos.distanceSqRt(packet.pos) >= 100) {
            /*
            return connection.disconnect(
                String.format(
                    "Invalid movement: (%s, %s, %s) -> (%s, %s, %s)",
                    currentPos.x, currentPos.y, currentPos.z,
                    packet.pos.x, packet.pos.y, packet.pos.z
                )
            )
             */
        }

        /*
         * don't let players move into unloaded areas.
         */
        val chunkPos = ChunkPos(BlockPos(packet.pos))
        val player = connection.player

        if (!player.world.chunks.isLoaded(chunkPos)) {
            connection.sendPacket(
                S2CSetAbsolutePlayerPositionPacket(
                    player.pos,
                    player.stance,
                    player.yaw,
                    player.pitch,
                    player.isOnGround,
                )
            )

            return
        }

        /*
        * if all is well, accept the new position.
        */
        withContext(server) {
            player.pos = packet.pos
//             player.stance = packet.stance
        }

        /*
         * also update the player's flying status.
         */
        handlePlayerFlyingStatus(packet)
    }

    override suspend fun handlePlayerRotation(packet: RotationPacket) {
        withContext(server) {
            connection.player.yaw = packet.yaw
            connection.player.pitch = packet.pitch
        }

        /*
        * also update the player's flying status.
        */
        handlePlayerFlyingStatus(packet)
    }

    override suspend fun handleAbsolutePlayerPosition(packet: C2SSetAbsolutePlayerPositionPacket) {
        handlePlayerPosition(packet)
        handlePlayerRotation(packet)
    }

    override suspend fun handlePlayerDig(packet: C2SPlayerDigPacket) = withContext(server) {
        when (packet.action) {
            C2SPlayerDigPacket.Action.START -> {}
            C2SPlayerDigPacket.Action.FINISH -> {
                /**
                 * todo;
                 * - perform range checks for block breaking (4 block radius?)
                 * - perform speed checks (are we breaking the block too fast, hacking?)
                 */
                player.world.breakBlock(packet.blockPos)
            }

            /**
             * Drop a single count out of the item stack the
             * player is currently holding in their hand.
             */
            C2SPlayerDigPacket.Action.DROP_ITEM -> {
                if (player.heldItem.isEmpty) return@withContext

                val (remainder, singleCount) = player.heldItem.split(1)
                player.heldItem = remainder
                player.drop(singleCount)
            }
        }
    }

    override suspend fun handleBlockPlacement(packet: C2SPlaceBlockPacket) = withContext(server) {
        if (!packet.isBlockPlacement()) {
            return@withContext
        }

        val item = player.heldItem.takeUnless(ItemStack::isEmpty)
            ?: return@withContext

        val offset = packet.face.offset
        val target = packet.blockPos.add(offset.x, offset.y, offset.z)

        /**
         * Check world collisions to make sure there is not another
         * block or an entity within the bounding box of the target block.
         */
        val currentState = player.world.getBlock(target)
        if (!currentState.isAir() || player.world.getCollidingEntities(target.boundingBox).any()) {
            player.connection.sendPacket(S2CUpdateBlockPacket(target, currentState))
            return@withContext
        }

        /**
         * todo; some considerations:
         * - make sure the player can actually reach the target block (4 block reach radius?)
         * - get the block from the registry based on id instead of making a new instance
         */
        player.world.setBlock(target, BlockState(Block(item.item.networkId), item.metadataValue))
        player.heldItem = item.shrink(1)
    }

    override suspend fun handlePlayerAction(packet: GenericPlayerActionPacket) {
        when (packet.action) {
            GenericPlayerActionPacket.Action.SWING_ARM -> connection.player.swingArm()
            else -> return
        }
    }

    override suspend fun handleHeldSlotChange(packet: C2SSetHeldSlotPacket) = withContext(server) {
        connection.player.selectedHotbarSlot = packet.slot
    }

    override suspend fun handleMenuInteraction(packet: C2SMenuInteractionPacket) = withContext(server) {
        val openMenu = player.menuSynchronizer.currentMenu

        if (packet.windowId != openMenu.id) {
            return@withContext connection.disconnect("Tried to interact with an invalid menu (#${packet.windowId})")
        }

        val state = openMenu.incrementState()
        if (state != packet.stateId) {
            player.world.server.logger.warn("menu state mismatch for ${player.username} ($state, ${packet.stateId})")
            connection.sendPacket(S2CMenuTransactionPacket(packet.windowId, packet.stateId, false))
            player.menuSynchronizer.synchronize()
            return@withContext
        }

        try {
            openMenu.interact(player, packet)
            connection.sendPacket(S2CMenuTransactionPacket(packet.windowId, packet.stateId, true))
        } catch (x: Throwable) {
            connection.sendPacket(S2CMenuTransactionPacket(packet.windowId, packet.stateId, false))
            player.menuSynchronizer.synchronize()
        }
    }

    override suspend fun handleMenuClose(packet: C2SCloseMenuPacket) = withContext(server) {
        player.menuSynchronizer.closeMenu(packet.id)
    }

    override suspend fun handlePing(packet: PingPacket) = withContext(server) {
        logger.info("handle meeeeee!!!")

        val msg = StringBuilder()
        logger.info("ping'd!")
        logger.info(packet.toString())

        when (packet.pingHostString) {
            "BTAPingHost" -> when (packet.payload.toInt()) {
                else -> msg.append("§1").append('\u0000').append(32786).append('\u0000').append(Global.VERSION)
                    .append('\u0000').append("Test").append('\u0000')
                    .append(server.playerList.onlinePlayers.size).append('\u0000')
                    .append(20)
            }
        }

        connection.sendPacket(GenericDisconnectPacket(msg.toString()))
        logger.info("sending kick with msg!")
    }

    override suspend fun handleDisconnect(packet: GenericDisconnectPacket) {
        connection.close()
    }
}