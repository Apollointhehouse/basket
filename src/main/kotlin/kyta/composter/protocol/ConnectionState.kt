package kyta.composter.protocol

import kyta.composter.protocol.io.PacketDefinitions

enum class ConnectionState {
    HANDSHAKING,
    LOGIN,
    PLAY;

    internal val definitions = mapOf(
        FlowDirection.CLIENTBOUND to PacketDefinitions(this),
        FlowDirection.SERVERBOUND to PacketDefinitions(this),
    )
}