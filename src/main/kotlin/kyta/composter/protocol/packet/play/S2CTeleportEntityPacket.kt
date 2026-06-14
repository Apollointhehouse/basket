package kyta.composter.protocol.packet.play

import kyta.composter.protocol.asRotation
import kyta.composter.math.Vec3d
import kyta.composter.protocol.Packet
import kyta.composter.protocol.io.PacketSerializer
import kyta.composter.protocol.io.WriteBuffer

class S2CTeleportEntityPacket(
    val id: Int,
    val pos: Vec3d,
    val yaw: Float,
    val pitch: Float,
) : Packet {
    companion object : PacketSerializer<S2CTeleportEntityPacket> {
        override fun serialize(packet: S2CTeleportEntityPacket, buffer: WriteBuffer) {
            buffer.writeInt(packet.id)
            buffer.writeAbsoluteInt(packet.pos.x)
            buffer.writeAbsoluteInt(packet.pos.y)
            buffer.writeAbsoluteInt(packet.pos.z)
            buffer.writeByte(packet.yaw.asRotation())
            buffer.writeByte(packet.pitch.asRotation())
        }
    }
}