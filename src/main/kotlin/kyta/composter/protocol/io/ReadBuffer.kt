package kyta.composter.protocol.io

import kyta.composter.world.BlockPos

interface ReadBuffer {
    fun readByte(): Byte
    fun readUByte(): Short
    fun readShort(): Short
    fun readInt(): Int
    fun readLong(): Long
    fun readFloat(): Float
    fun readDouble(): Double
    fun readBoolean(): Boolean
    fun readString(): String
    fun readBlockPos(): BlockPos
}