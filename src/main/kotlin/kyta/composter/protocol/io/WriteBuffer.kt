package kyta.composter.protocol.io

import kyta.composter.world.BlockPos

interface WriteBuffer {
    fun writeByte(value: Number)
    fun writeUByte(value: Number)
    fun writeBytes(value: ByteArray, start: Int, end: Int)
    fun writeShort(value: Number)
    fun writeInt(value: Number)
    fun writeAbsoluteInt(value: Number)
    fun writeLong(value: Number)
    fun writeFloat(value: Number)
    fun writeDouble(value: Number)
    fun writeBoolean(value: Boolean)
    fun writeString(value: String)
    fun writeBlockPos(value: BlockPos)
}