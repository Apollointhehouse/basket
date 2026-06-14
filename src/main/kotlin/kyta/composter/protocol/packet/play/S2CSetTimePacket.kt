package kyta.composter.protocol.packet.play

import kyta.composter.protocol.Packet
import kyta.composter.protocol.io.PacketSerializer
import kyta.composter.protocol.io.WriteBuffer

data class S2CSetTimePacket(val time: Long) : Packet {
    companion object : PacketSerializer<S2CSetTimePacket> {
        override fun serialize(packet: S2CSetTimePacket, buffer: WriteBuffer) {
            buffer.writeLong(packet.time)
        }
    }
}