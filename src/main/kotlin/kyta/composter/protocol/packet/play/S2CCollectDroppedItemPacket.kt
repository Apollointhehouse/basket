package kyta.composter.protocol.packet.play

import kyta.composter.protocol.Packet
import kyta.composter.protocol.io.PacketSerializer
import kyta.composter.protocol.io.WriteBuffer

data class S2CCollectDroppedItemPacket(
    val id: Int,
    val collector: Int,
) : Packet {
    companion object : PacketSerializer<S2CCollectDroppedItemPacket> {
        override fun serialize(packet: S2CCollectDroppedItemPacket, buffer: WriteBuffer) {
            buffer.writeInt(packet.id)
            buffer.writeInt(packet.collector)
        }
    }
}