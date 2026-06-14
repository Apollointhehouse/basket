package kyta.composter.protocol.packet.play

import kyta.composter.protocol.PacketHandler
import kyta.composter.protocol.io.PacketSerializer
import kyta.composter.protocol.io.ReadBuffer
import kyta.composter.protocol.ServerboundPacket

class C2SSetHeldSlotPacket(val slot: Int) : ServerboundPacket {
    override suspend fun handle(handler: PacketHandler) = handler.handleHeldSlotChange(this)

    companion object : PacketSerializer<C2SSetHeldSlotPacket> {
        override fun deserialize(buffer: ReadBuffer): C2SSetHeldSlotPacket {
            return C2SSetHeldSlotPacket(buffer.readShort().coerceIn(0, 8).toInt())
        }
    }
}