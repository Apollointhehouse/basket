package kyta.composter.protocol.io

import kyta.composter.protocol.Packet

interface PacketSerializer<out T : Packet> {
    fun serialize(packet: @UnsafeVariance T, buffer: WriteBuffer) {
    }

    fun deserialize(buffer: ReadBuffer): T {
        throw UnsupportedOperationException()
    }
}