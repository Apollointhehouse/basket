package kyta.composter.protocol.packet

import kyta.composter.protocol.PacketHandler
import kyta.composter.protocol.io.PacketSerializer
import kyta.composter.protocol.io.ReadBuffer
import kyta.composter.protocol.ServerboundPacket

object GenericKeepAlivePacket : ServerboundPacket, PacketSerializer<GenericKeepAlivePacket> {
    override suspend fun handle(handler: PacketHandler) = handler.handleKeepAlive(this)
    override fun deserialize(buffer: ReadBuffer): GenericKeepAlivePacket {
        return this
    }
}