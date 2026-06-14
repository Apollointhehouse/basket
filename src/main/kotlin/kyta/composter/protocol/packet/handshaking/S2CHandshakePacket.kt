package kyta.composter.protocol.packet.handshaking

import kyta.composter.protocol.Packet
import kyta.composter.protocol.io.PacketSerializer
import kyta.composter.protocol.io.WriteBuffer

data class S2CHandshakePacket(val hash: String) : Packet {
    companion object : PacketSerializer<S2CHandshakePacket> {
        override fun serialize(packet: S2CHandshakePacket, buffer: WriteBuffer) {
            buffer.writeString(packet.hash)
        }
    }
}