package kyta.composter.protocol.packet.play

import kyta.composter.protocol.Packet
import kyta.composter.protocol.io.PacketSerializer
import kyta.composter.protocol.io.WriteBuffer
import kyta.composter.world.entity.Entity

data class S2CRemoveEntityPacket(val id: Int) : Packet {
    constructor(entity: Entity) : this(entity.id)

    companion object : PacketSerializer<S2CRemoveEntityPacket> {
        override fun serialize(packet: S2CRemoveEntityPacket, buffer: WriteBuffer) {
            buffer.writeInt(packet.id)
        }
    }
}