package kyta.composter.protocol.packet.play

import kyta.composter.protocol.asAbsoluteInt
import kyta.composter.protocol.asRotation
import kyta.composter.protocol.Packet
import kyta.composter.protocol.io.PacketSerializer
import kyta.composter.protocol.io.WriteBuffer

interface EntityRelativePositionPacket : Packet {
    val deltaX: Double
    val deltaY: Double
    val deltaZ: Double
}

interface EntityRotationPacket : Packet {
    val yaw: Float
    val pitch: Float
}

data class S2CSetEntityRotationPacket(
    private val id: Int,
    override val yaw: Float,
    override val pitch: Float,
): EntityRotationPacket {
    companion object : PacketSerializer<S2CSetEntityRotationPacket> {
        override fun serialize(packet: S2CSetEntityRotationPacket, buffer: WriteBuffer) {
            buffer.writeInt(packet.id)
            buffer.writeByte(packet.yaw.asRotation())
            buffer.writeByte(packet.pitch.asRotation())
        }
    }
}

data class S2CAdjustEntityPositionPacket(
    val id: Int,
    override val deltaX: Double,
    override val deltaY: Double,
    override val deltaZ: Double,
) : EntityRelativePositionPacket {
    companion object : PacketSerializer<S2CAdjustEntityPositionPacket> {
        override fun serialize(packet: S2CAdjustEntityPositionPacket, buffer: WriteBuffer) {
            buffer.writeInt(packet.id)
            buffer.writeByte(packet.deltaX.asAbsoluteInt())
            buffer.writeByte(packet.deltaY.asAbsoluteInt())
            buffer.writeByte(packet.deltaZ.asAbsoluteInt())
        }
    }
}

data class S2CAdjustEntityPositionRotationPacket(
    val id: Int,
    override val deltaX: Double,
    override val deltaY: Double,
    override val deltaZ: Double,
    override val yaw: Float,
    override val pitch: Float,
) : EntityRelativePositionPacket, EntityRotationPacket {
    companion object : PacketSerializer<S2CAdjustEntityPositionRotationPacket> {
        override fun serialize(packet: S2CAdjustEntityPositionRotationPacket, buffer: WriteBuffer) {
            buffer.writeInt(packet.id)
            buffer.writeByte(packet.deltaX.asAbsoluteInt())
            buffer.writeByte(packet.deltaY.asAbsoluteInt())
            buffer.writeByte(packet.deltaZ.asAbsoluteInt())
            buffer.writeByte(packet.yaw.asRotation())
            buffer.writeByte(packet.pitch.asRotation())
        }
    }
}