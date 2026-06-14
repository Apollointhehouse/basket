package kyta.composter.protocol

interface ServerboundPacket : Packet {
    suspend fun handle(handler: PacketHandler)
}