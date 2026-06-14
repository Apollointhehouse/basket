package kyta.composter.protocol.packet.play

import kyta.composter.protocol.Packet
import kyta.composter.protocol.io.PacketSerializer
import kyta.composter.protocol.io.WriteBuffer
import kyta.composter.world.BlockPos

data class S2CSetSpawnPacket(val pos: BlockPos) : Packet {
    companion object : PacketSerializer<S2CSetSpawnPacket> {
        override fun serialize(packet: S2CSetSpawnPacket, buffer: WriteBuffer) {
            buffer.writeInt(packet.pos.x)
            buffer.writeInt(packet.pos.y) // must be an integer y-value
            buffer.writeInt(packet.pos.z)
        }
    }
}