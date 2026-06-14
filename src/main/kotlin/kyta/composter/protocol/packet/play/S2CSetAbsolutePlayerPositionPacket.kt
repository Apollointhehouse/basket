package kyta.composter.protocol.packet.play

import kyta.composter.math.Vec3d
import kyta.composter.protocol.Packet
import kyta.composter.protocol.io.PacketSerializer
import kyta.composter.protocol.io.WriteBuffer

data class S2CSetAbsolutePlayerPositionPacket(
    val pos: Vec3d,
    val stance: Double,
    val yaw: Float,
    val pitch: Float,
    val onGround: Boolean,
) : Packet {
    companion object : PacketSerializer<S2CSetAbsolutePlayerPositionPacket> {
        override fun serialize(packet: S2CSetAbsolutePlayerPositionPacket, buffer: WriteBuffer) {
            buffer.writeDouble(packet.pos.x)
            buffer.writeDouble(packet.pos.y)
            buffer.writeDouble(packet.stance)
            buffer.writeDouble(packet.pos.z)
            buffer.writeFloat(packet.yaw)
            buffer.writeFloat(packet.pitch)
            buffer.writeBoolean(packet.onGround)
        }
    }
}