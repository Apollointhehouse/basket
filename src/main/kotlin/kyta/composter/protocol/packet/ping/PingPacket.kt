package kyta.composter.protocol.packet.ping

import kyta.composter.protocol.PacketHandler
import kyta.composter.protocol.io.PacketSerializer
import kyta.composter.protocol.io.ReadBuffer
import kyta.composter.protocol.ServerboundPacket

data class PingPacket(
    val payload: Short = 0,
    val identifier: Short = 0,
    val pingHostString: String? = null,
    val protocolVersion: Short = 0,
    val hostname: String? = null,
    val port: Short = 0
) : ServerboundPacket {
    override suspend fun handle(handler: PacketHandler) = handler.handlePing(this)

    companion object : PacketSerializer<PingPacket> {
        override fun deserialize(buffer: ReadBuffer): PingPacket {
            return PingPacket(buffer.readUByte(), buffer.readUByte(), buffer.readString(), buffer.readUByte(), buffer.readString(), buffer.readUByte())
        }
    }
}