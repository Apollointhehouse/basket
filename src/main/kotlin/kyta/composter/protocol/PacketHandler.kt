package kyta.composter.protocol

import kyta.composter.protocol.packet.GenericDisconnectPacket
import kyta.composter.protocol.packet.GenericKeepAlivePacket
import kyta.composter.protocol.packet.handshaking.C2SHandshakePacket
import kyta.composter.protocol.packet.login.C2SLoginPacket
import kyta.composter.protocol.packet.ping.PingPacket
import kyta.composter.protocol.packet.play.FlyingStatusPacket
import kyta.composter.protocol.packet.play.GenericPlayerActionPacket
import kyta.composter.protocol.packet.play.PositionPacket
import kyta.composter.protocol.packet.play.RotationPacket
import kyta.composter.protocol.packet.play.C2SChatMessagePacket
import kyta.composter.protocol.packet.play.C2SCloseMenuPacket
import kyta.composter.protocol.packet.play.C2SEntityActionPacket
import kyta.composter.protocol.packet.play.C2SMenuInteractionPacket
import kyta.composter.protocol.packet.play.C2SPlaceBlockPacket
import kyta.composter.protocol.packet.play.C2SPlayerDigPacket
import kyta.composter.protocol.packet.play.ServerboundSetAbsolutePlayerPositionPacket
import kyta.composter.protocol.packet.play.C2SSetHeldSlotPacket

interface PacketHandler {
    suspend fun handleHandshake(packet: C2SHandshakePacket)
    suspend fun handleKeepAlive(packet: GenericKeepAlivePacket)
    suspend fun handleLogin(packet: C2SLoginPacket)

    suspend fun handleChatMessage(packet: C2SChatMessagePacket)
    suspend fun handleEntityAction(packet: C2SEntityActionPacket)

    suspend fun handlePlayerFlyingStatus(packet: FlyingStatusPacket)
    suspend fun handlePlayerPosition(packet: PositionPacket)
    suspend fun handlePlayerRotation(packet: RotationPacket)
    suspend fun handleAbsolutePlayerPosition(packet: ServerboundSetAbsolutePlayerPositionPacket)

    suspend fun handlePlayerDig(packet: C2SPlayerDigPacket)
    suspend fun handleBlockPlacement(packet: C2SPlaceBlockPacket)
    suspend fun handlePlayerAction(packet: GenericPlayerActionPacket)

    suspend fun handleHeldSlotChange(packet: C2SSetHeldSlotPacket)
    suspend fun handleMenuInteraction(packet: C2SMenuInteractionPacket)
    suspend fun handleMenuClose(packet: C2SCloseMenuPacket)

    suspend fun handlePing(packet: PingPacket)
    suspend fun handleDisconnect(packet: GenericDisconnectPacket)
}