package kyta.composter.protocol.packet.login

import kyta.composter.protocol.PacketHandler
import kyta.composter.protocol.io.PacketSerializer
import kyta.composter.protocol.io.ReadBuffer
import kyta.composter.protocol.ServerboundPacket

data class C2SLoginPacket(val protocolVersion: Int, val username: String) : ServerboundPacket {
    override suspend fun handle(handler: PacketHandler) = handler.handleLogin(this)

    companion object : PacketSerializer<C2SLoginPacket> {
        override fun deserialize(buffer: ReadBuffer): C2SLoginPacket {
            return C2SLoginPacket(buffer.readInt(), buffer.readString())
        }
    }
}