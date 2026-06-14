package kyta.composter.protocol

import kyta.composter.protocol.io.PacketSerializer
import kyta.composter.protocol.packet.GenericDisconnectPacket
import kyta.composter.protocol.packet.GenericKeepAlivePacket
import kyta.composter.protocol.packet.handshaking.S2CHandshakePacket
import kyta.composter.protocol.packet.handshaking.C2SHandshakePacket
import kyta.composter.protocol.packet.login.S2CLoginPacket
import kyta.composter.protocol.packet.login.C2SLoginPacket
import kyta.composter.protocol.packet.ping.PingPacket
import kyta.composter.protocol.packet.play.S2CAddDroppedItemPacket
import kyta.composter.protocol.packet.play.S2CAddEntityPacket
import kyta.composter.protocol.packet.play.S2CAddPlayerPacket
import kyta.composter.protocol.packet.play.S2CAdjustEntityPositionPacket
import kyta.composter.protocol.packet.play.S2CAdjustEntityPositionRotationPacket
import kyta.composter.protocol.packet.play.S2CChatMessagePacket
import kyta.composter.protocol.packet.play.S2CChunkDataPacket
import kyta.composter.protocol.packet.play.S2CChunkOperationPacket
import kyta.composter.protocol.packet.play.S2CCollectDroppedItemPacket
import kyta.composter.protocol.packet.play.S2CRemoveEntityPacket
import kyta.composter.protocol.packet.play.S2CSetAbsolutePlayerPositionPacket
import kyta.composter.protocol.packet.play.S2CSetContainerContentPacket
import kyta.composter.protocol.packet.play.S2CSetEntityRotationPacket
import kyta.composter.protocol.packet.play.S2CSetSpawnPacket
import kyta.composter.protocol.packet.play.S2CSetTimePacket
import kyta.composter.protocol.packet.play.S2CTeleportEntityPacket
import kyta.composter.protocol.packet.play.S2CUpdateBlockPacket
import kyta.composter.protocol.packet.play.GenericPlayerActionPacket
import kyta.composter.protocol.packet.play.C2SChatMessagePacket
import kyta.composter.protocol.packet.play.C2SCloseMenuPacket
import kyta.composter.protocol.packet.play.C2SMenuInteractionPacket
import kyta.composter.protocol.packet.play.C2SPlaceBlockPacket
import kyta.composter.protocol.packet.play.C2SPlayerDigPacket
import kyta.composter.protocol.packet.play.C2SSetAbsolutePlayerPositionPacket
import kyta.composter.protocol.packet.play.C2SSetHeldSlotPacket
import kyta.composter.protocol.packet.play.C2SSetPlayerFlyingStatusPacket
import kyta.composter.protocol.packet.play.C2SSetPlayerPositionPacket
import kyta.composter.protocol.packet.play.C2SSetPlayerRotationPacket
import kotlin.reflect.KClass
import kyta.composter.protocol.packet.play.S2CMenuTransactionPacket
import kyta.composter.protocol.packet.play.S2CSetEntityDataPacket
import kyta.composter.protocol.packet.play.C2SEntityActionPacket
import kotlin.reflect.full.companionObjectInstance

object Protocol {
    fun bootstrap() {
        val allStates = ConnectionState.entries.toTypedArray()

        register<GenericKeepAlivePacket>(0, FlowDirection.CLIENTBOUND, *allStates)
        register<GenericKeepAlivePacket>(0, FlowDirection.SERVERBOUND, *allStates)

        register<S2CLoginPacket>(1, FlowDirection.CLIENTBOUND, ConnectionState.LOGIN)
        register<C2SLoginPacket>(1, FlowDirection.SERVERBOUND, ConnectionState.LOGIN)

        register<S2CHandshakePacket>(2, FlowDirection.CLIENTBOUND, ConnectionState.HANDSHAKING)
        register<C2SHandshakePacket>(2, FlowDirection.SERVERBOUND, ConnectionState.HANDSHAKING)

        register<S2CChatMessagePacket>(3, FlowDirection.CLIENTBOUND, ConnectionState.PLAY)
        register<C2SChatMessagePacket>(3, FlowDirection.SERVERBOUND, ConnectionState.PLAY)
        register<S2CSetTimePacket>(4, FlowDirection.CLIENTBOUND, ConnectionState.PLAY)
        register<S2CSetSpawnPacket>(6, FlowDirection.CLIENTBOUND, ConnectionState.PLAY)
        register<C2SSetPlayerFlyingStatusPacket>(10, FlowDirection.SERVERBOUND, ConnectionState.PLAY)
        register<C2SSetPlayerPositionPacket>(11, FlowDirection.SERVERBOUND, ConnectionState.PLAY)
        register<C2SSetPlayerRotationPacket>(12, FlowDirection.SERVERBOUND, ConnectionState.PLAY)
        register<S2CSetAbsolutePlayerPositionPacket>(13, FlowDirection.CLIENTBOUND, ConnectionState.PLAY)
        register<C2SSetAbsolutePlayerPositionPacket>(13, FlowDirection.SERVERBOUND, ConnectionState.PLAY)
        register<C2SPlayerDigPacket>(14, FlowDirection.SERVERBOUND, ConnectionState.PLAY)
        register<C2SPlaceBlockPacket>(15, FlowDirection.SERVERBOUND, ConnectionState.PLAY)
        register<C2SSetHeldSlotPacket>(16, FlowDirection.SERVERBOUND, ConnectionState.PLAY)
        register<GenericPlayerActionPacket>(18, FlowDirection.CLIENTBOUND, ConnectionState.PLAY)
        register<GenericPlayerActionPacket>(18, FlowDirection.SERVERBOUND, ConnectionState.PLAY)
        register<C2SEntityActionPacket>(19, FlowDirection.SERVERBOUND, ConnectionState.PLAY)
        register<S2CAddPlayerPacket>(20, FlowDirection.CLIENTBOUND, ConnectionState.PLAY)
        register<S2CAddDroppedItemPacket>(21, FlowDirection.CLIENTBOUND, ConnectionState.PLAY)
        register<S2CCollectDroppedItemPacket>(22, FlowDirection.CLIENTBOUND, ConnectionState.PLAY)
        register<S2CAddEntityPacket>(24, FlowDirection.CLIENTBOUND, ConnectionState.PLAY)
        register<S2CRemoveEntityPacket>(29, FlowDirection.CLIENTBOUND, ConnectionState.PLAY)
        register<S2CAdjustEntityPositionPacket>(31, FlowDirection.CLIENTBOUND, ConnectionState.PLAY)
        register<S2CSetEntityRotationPacket>(32, FlowDirection.CLIENTBOUND, ConnectionState.PLAY)
        register<S2CAdjustEntityPositionRotationPacket>(33, FlowDirection.CLIENTBOUND, ConnectionState.PLAY)
        register<S2CTeleportEntityPacket>(34, FlowDirection.CLIENTBOUND, ConnectionState.PLAY)
        register<S2CSetEntityDataPacket>(40, FlowDirection.CLIENTBOUND, ConnectionState.PLAY)
        register<S2CChunkOperationPacket>(50, FlowDirection.CLIENTBOUND, ConnectionState.PLAY)
        register<S2CChunkDataPacket>(51, FlowDirection.CLIENTBOUND, ConnectionState.PLAY)
        register<S2CUpdateBlockPacket>(53, FlowDirection.CLIENTBOUND, ConnectionState.PLAY)
        register<C2SCloseMenuPacket>(101, FlowDirection.SERVERBOUND, ConnectionState.PLAY)
        register<C2SMenuInteractionPacket>(102, FlowDirection.SERVERBOUND, ConnectionState.PLAY)
        register<S2CSetContainerContentPacket>(104, FlowDirection.CLIENTBOUND, ConnectionState.PLAY)
        register<S2CMenuTransactionPacket>(106, FlowDirection.CLIENTBOUND, ConnectionState.PLAY)

        register<PingPacket>(254, FlowDirection.SERVERBOUND, *allStates)

        register<GenericDisconnectPacket>(255, FlowDirection.CLIENTBOUND, *allStates)
        register<GenericDisconnectPacket>(255, FlowDirection.SERVERBOUND, *allStates)
    }

    private fun register(
        id: Int,
        type: KClass<out Packet>,
        serializer: PacketSerializer<Packet>,
        direction: FlowDirection,
        vararg states: ConnectionState,
    ) {
        states.forEach {
            val definitions = it.definitions[direction]!!
            definitions.packetIds[type] = id
            definitions.serializers[id] = serializer
        }
    }

    private inline fun <reified T : Packet> register(
        id: Int,
        direction: FlowDirection,
        vararg states: ConnectionState,
    ) {
        register(id, T::class, serializer<T>(), direction, *states)
    }

    private inline fun <reified T : Packet> serializer(): PacketSerializer<T> =
        serializer(T::class)

    @Suppress("UNCHECKED_CAST")
    private fun <T : Packet> serializer(clazz: KClass<*>): PacketSerializer<T> {
        if (clazz.objectInstance is PacketSerializer<*>) {
            return clazz.objectInstance as PacketSerializer<T>
        }

        if (clazz.companionObjectInstance is PacketSerializer<*>) {
            return clazz.companionObjectInstance as PacketSerializer<T>
        }

        throw IllegalArgumentException("Received type must have serializer")
    }
}