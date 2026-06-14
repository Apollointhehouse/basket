package kyta.composter.protocol.packet.play

import kyta.composter.protocol.Packet
import kyta.composter.protocol.io.PacketSerializer
import kyta.composter.protocol.io.WriteBuffer
import kyta.composter.world.BlockPos
import kyta.composter.world.block.BlockState

data class S2CUpdateBlockPacket(val blockPos: BlockPos, val blockState: BlockState) : Packet {
    companion object : PacketSerializer<S2CUpdateBlockPacket> {
        override fun serialize(packet: S2CUpdateBlockPacket, buffer: WriteBuffer) {
            buffer.writeBlockPos(packet.blockPos)
            buffer.writeByte(packet.blockState.block.networkId)
            buffer.writeByte(packet.blockState.metadataValue)
        }
    }
}