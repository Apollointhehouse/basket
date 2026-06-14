package kyta.composter.protocol.packet.play

import kyta.composter.protocol.Packet
import kyta.composter.protocol.io.PacketSerializer
import kyta.composter.protocol.io.WriteBuffer

data class S2CMenuTransactionPacket(
    val menuId: Int,
    val stateId: Int,
    val successful: Boolean,
) : Packet {
    companion object : PacketSerializer<S2CMenuTransactionPacket> {
        override fun serialize(packet: S2CMenuTransactionPacket, buffer: WriteBuffer) {
            buffer.writeByte(packet.menuId)
            buffer.writeShort(packet.stateId)
            buffer.writeBoolean(packet.successful)
        }
    }
}