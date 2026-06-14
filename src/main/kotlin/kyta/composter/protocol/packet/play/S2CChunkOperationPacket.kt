package kyta.composter.protocol.packet.play

import kyta.composter.protocol.Packet
import kyta.composter.protocol.io.PacketSerializer
import kyta.composter.protocol.io.WriteBuffer
import kyta.composter.world.ChunkPos

data class S2CChunkOperationPacket(val pos: ChunkPos, val mode: Mode) : Packet {
    enum class Mode {
        LOAD,
        UNLOAD,
    }

    companion object : PacketSerializer<S2CChunkOperationPacket> {
        override fun serialize(packet: S2CChunkOperationPacket, buffer: WriteBuffer) {
            buffer.writeInt(packet.pos.x)
            buffer.writeInt(packet.pos.z)
            buffer.writeBoolean(packet.mode == Mode.LOAD)
        }
    }
}
