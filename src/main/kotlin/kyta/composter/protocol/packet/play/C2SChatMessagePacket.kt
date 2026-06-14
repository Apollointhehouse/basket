package kyta.composter.protocol.packet.play

import kyta.composter.protocol.PacketHandler
import kyta.composter.protocol.io.PacketSerializer
import kyta.composter.protocol.io.ReadBuffer
import kyta.composter.protocol.ServerboundPacket

data class C2SChatMessagePacket(val message: String) : ServerboundPacket {
    override suspend fun handle(handler: PacketHandler) = handler.handleChatMessage(this)

    companion object : PacketSerializer<C2SChatMessagePacket> {
        override fun deserialize(buffer: ReadBuffer): C2SChatMessagePacket {
            return C2SChatMessagePacket(buffer.readString())
        }
    }
}