package kyta.composter.protocol.packet.play

import kyta.composter.math.Vec3d
import kyta.composter.protocol.PacketHandler
import kyta.composter.protocol.io.PacketSerializer
import kyta.composter.protocol.io.ReadBuffer
import kyta.composter.protocol.ServerboundPacket

interface FlyingStatusPacket {
    val onGround: Boolean
}

interface PositionPacket : FlyingStatusPacket {
    val pos: Vec3d
    val stance: Double
}

interface RotationPacket : FlyingStatusPacket {
    val yaw: Float
    val pitch: Float
}

// --

data class C2SSetPlayerFlyingStatusPacket(
    override val onGround: Boolean
) : ServerboundPacket, FlyingStatusPacket {
    override suspend fun handle(handler: PacketHandler) = handler.handlePlayerFlyingStatus(this)

    companion object : PacketSerializer<C2SSetPlayerFlyingStatusPacket> {
        override fun deserialize(buffer: ReadBuffer): C2SSetPlayerFlyingStatusPacket {
            return C2SSetPlayerFlyingStatusPacket(buffer.readBoolean())
        }
    }
}

data class C2SSetPlayerPositionPacket(
    override val pos: Vec3d,
    override val stance: Double,
    override val onGround: Boolean,
) : ServerboundPacket, PositionPacket {
    override suspend fun handle(handler: PacketHandler) = handler.handlePlayerPosition(this)

    companion object : PacketSerializer<C2SSetPlayerPositionPacket> {
        override fun deserialize(buffer: ReadBuffer): C2SSetPlayerPositionPacket {
            val x = buffer.readDouble()
            val y = buffer.readDouble()
            val stance = buffer.readDouble()
            val z = buffer.readDouble()

            return C2SSetPlayerPositionPacket(
                Vec3d(x, y, z),
                stance,
                buffer.readBoolean(),
            )
        }
    }
}

data class C2SSetPlayerRotationPacket(
    override val yaw: Float,
    override val pitch : Float,
    override val onGround: Boolean,
) : ServerboundPacket, RotationPacket {
    override suspend fun handle(handler: PacketHandler) = handler.handlePlayerRotation(this)

    companion object : PacketSerializer<C2SSetPlayerRotationPacket> {
        override fun deserialize(buffer: ReadBuffer): C2SSetPlayerRotationPacket {
            return C2SSetPlayerRotationPacket(
                buffer.readFloat(),
                buffer.readFloat(),
                buffer.readBoolean(),
            )
        }
    }
}

data class C2SSetAbsolutePlayerPositionPacket(
    override val pos: Vec3d,
    override val stance: Double,
    override val yaw: Float,
    override val pitch: Float,
    override val onGround: Boolean,
) : ServerboundPacket, PositionPacket, RotationPacket {
    override suspend fun handle(handler: PacketHandler) = handler.handleAbsolutePlayerPosition(this)

    companion object : PacketSerializer<C2SSetAbsolutePlayerPositionPacket> {
        override fun deserialize(buffer: ReadBuffer): C2SSetAbsolutePlayerPositionPacket {
            val x = buffer.readDouble()
            val y = buffer.readDouble()
            val stance = buffer.readDouble()
            val z = buffer.readDouble()

            return C2SSetAbsolutePlayerPositionPacket(
                Vec3d(x, y, z),
                stance,
                buffer.readFloat(),
                buffer.readFloat(),
                buffer.readBoolean(),
            )
        }
    }
}
