package kyta.composter.protocol.packet.login

import kyta.composter.protocol.Packet
import kyta.composter.protocol.io.PacketSerializer
import kyta.composter.protocol.io.WriteBuffer
import kyta.composter.world.dimension.DimensionType

data class S2CLoginPacket(
    val entityId: Int,
    val serverId: String,
    val seed: Long,
    val dimension: DimensionType,
) : Packet {
    companion object : PacketSerializer<S2CLoginPacket> {
        override fun serialize(packet: S2CLoginPacket, buffer: WriteBuffer) {
            buffer.writeInt(packet.entityId)
            buffer.writeString(packet.serverId)
            buffer.writeLong(packet.seed)
            buffer.writeByte(packet.dimension.id)
        }
    }
}