package kyta.composter.protocol.packet.handshaking

import kyta.composter.protocol.PacketHandler
import kyta.composter.protocol.io.PacketSerializer
import kyta.composter.protocol.io.ReadBuffer
import kyta.composter.protocol.ServerboundPacket

data class C2SHandshakePacket(val username: String) : ServerboundPacket {
    override suspend fun handle(handler: PacketHandler) = handler.handleHandshake(this)

    companion object : PacketSerializer<C2SHandshakePacket> {
        override fun deserialize(buffer: ReadBuffer): C2SHandshakePacket {
            return C2SHandshakePacket(buffer.readString())
        }
    }
}