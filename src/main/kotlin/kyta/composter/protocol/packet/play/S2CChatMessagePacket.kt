package kyta.composter.protocol.packet.play

import kyta.composter.protocol.Packet
import kyta.composter.protocol.io.PacketSerializer
import kyta.composter.protocol.io.WriteBuffer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

data class S2CChatMessagePacket(val message: Component): Packet {
    companion object : PacketSerializer<S2CChatMessagePacket> {
        override fun serialize(packet: S2CChatMessagePacket, buffer: WriteBuffer) {
            buffer.writeString(LegacyComponentSerializer.legacySection().serialize(packet.message))
        }
    }
}