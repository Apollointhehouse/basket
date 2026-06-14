package kyta.composter.protocol.packet.play

import kyta.composter.container.menu.Menu
import kyta.composter.item.isEmpty
import kyta.composter.protocol.Packet
import kyta.composter.protocol.io.PacketSerializer
import kyta.composter.protocol.io.WriteBuffer

data class S2CSetContainerContentPacket(
    val menuId: Int,
    val menu: Menu,
) : Packet {
    companion object : PacketSerializer<S2CSetContainerContentPacket> {
        override fun serialize(packet: S2CSetContainerContentPacket, buffer: WriteBuffer) {
            buffer.writeByte(packet.menuId)

            val slots = packet.menu.slots
            buffer.writeShort(slots.size)

            for (slot in slots) {
                val stack = slot.item

                if (stack.isEmpty) {
                    buffer.writeShort(-1)
                    continue
                }

                buffer.writeShort(stack.item.networkId)
                buffer.writeByte(stack.count)
                buffer.writeShort(stack.metadataValue)
            }
        }
    }
}