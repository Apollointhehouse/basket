package kyta.composter.protocol.io

import kyta.composter.protocol.ConnectionState
import kyta.composter.protocol.Packet
import kotlin.reflect.KClass

internal class PacketDefinitions(
    private val state: ConnectionState,
    internal val packetIds: MutableMap<KClass<out Packet>, Int> = mutableMapOf(),
    internal val serializers: Array<PacketSerializer<out Packet>?> = arrayOfNulls(256),
) {
    fun getId(type: KClass<out Packet>): Int {
        return packetIds[type] ?: error("${type.simpleName} does not have an assigned id for state ${state.name}.")
    }

    fun getSerializer(id: Int) : PacketSerializer<out Packet>? {
        return serializers[id]
    }
}