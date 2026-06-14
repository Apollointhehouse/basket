package kyta.composter.protocol.io

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import kyta.composter.protocol.asAbsoluteInt
import kyta.composter.world.BlockPos

@JvmInline
value class MinecraftPacketBuffer(internal val buf: ByteBuf) : ReadBuffer, WriteBuffer {
    override fun readByte(): Byte {
        return buf.readByte()
    }

    override fun readUByte(): Short {
        return buf.readUnsignedByte()
    }

    override fun readShort(): Short {
        return buf.readShort()
    }

    override fun readInt(): Int {
        return buf.readInt()
    }

    override fun readLong(): Long {
        return buf.readLong()
    }

    override fun readFloat(): Float {
        return buf.readFloat()
    }

    override fun readDouble(): Double {
        return buf.readDouble()
    }

    override fun readBoolean(): Boolean {
        return buf.readBoolean()
    }

    override fun readString(): String {
        val length = buf.readUnsignedShort()
        val characters = CharArray(length)

        for (i in 0 until length) {
            characters[i] = buf.readChar()
        }

        return String(characters)
    }

    override fun readBlockPos(): BlockPos {
        return BlockPos(
            buf.readInt(),
            buf.readByte().toInt(),
            buf.readInt(),
        )
    }

    override fun writeByte(value: Number) {
        buf.writeByte(value.toInt())
    }

    override fun writeUByte(value: Number) {
        buf.writeByte(value.toInt())
    }

    override fun writeBytes(value: ByteArray, start: Int, end: Int) {
        buf.writeBytes(value, start, end)
    }

    override fun writeShort(value: Number) {
        buf.writeShort(value.toInt())
    }

    override fun writeInt(value: Number) {
        buf.writeInt(value.toInt())
    }

    override fun writeAbsoluteInt(value: Number) {
        buf.writeInt(value.toDouble().asAbsoluteInt())
    }

    override fun writeLong(value: Number) {
        buf.writeLong(value.toLong())
    }

    override fun writeFloat(value: Number) {
        buf.writeFloat(value.toFloat())
    }

    override fun writeDouble(value: Number) {
        buf.writeDouble(value.toDouble())
    }

    override fun writeBoolean(value: Boolean) {
        buf.writeBoolean(value)
    }

    override fun writeString(value: String) {
        val characters = value.toCharArray()
        buf.writeShort(characters.size)

        for (character in characters) {
            buf.writeChar(character.code)
        }
    }

    override fun writeBlockPos(value: BlockPos) {
        buf.writeInt(value.x)
        buf.writeByte(value.y)
        buf.writeInt(value.z)
    }

    companion object {
        fun create(packetId: Int) = MinecraftPacketBuffer(Unpooled.buffer(1).writeByte(packetId))
    }
}