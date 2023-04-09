package org.kebab.api.packet.io;

import net.querz.nbt.io.NBTOutputStream;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.Tag;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.BitSet;
import java.util.EnumSet;
import java.util.UUID;

public final class KebabOutputStream extends DataOutputStream {
    private final ByteArrayOutputStream outputBuffer;
    public KebabOutputStream(ByteArrayOutputStream outputBuffer) {
        super(outputBuffer);
        this.outputBuffer = outputBuffer;
    }

    public KebabOutputStream() {
        this(new ByteArrayOutputStream());
    }

    public void writeString(String string) throws IOException {
        byte[] bytes = string.getBytes();
        writeVarInt(bytes.length);
        write(bytes);
    }

    public void writeVarInt(int value) throws IOException {
        while((value & -128) != 0) {
            writeByte(value & 127 | 128);
            value >>>= 7;
        }
        this.writeByte(value);
    }

    public void writeYawOrPitch(float yawOrPitch) throws IOException {
        writeByte((byte) (int) (yawOrPitch * 256.0F / 360.0F));
    }

    public <E extends Enum<E>> void writeEnumSet(EnumSet<E> enumSet, Class<E> clazz) throws IOException {
        E[] ae = clazz.getEnumConstants();
        BitSet bitSet = new BitSet(ae.length);

        for (int i = 0; i < ae.length; ++i) {
            bitSet.set(i, enumSet.contains(ae[i]));
        }

        writeFixedBitSet(bitSet, ae.length);
    }

    public void writeFixedBitSet(BitSet bitset, int i) throws IOException {
        if (bitset.length() > i) {
            int j = bitset.length();
            throw new RuntimeException("BitSet is larger than expected size (" + j + ">" + i + ")");
        } else {
            byte[] abyte = bitset.toByteArray();
            write(Arrays.copyOf(abyte, -Math.floorDiv(-i, 8)));
        }
    }

    public void writeUUID(UUID uuid) throws IOException {
        writeLong(uuid.getMostSignificantBits());
        writeLong(uuid.getLeastSignificantBits());
    }

    public void writeCompoundTag(CompoundTag tag) throws IOException {
        if (tag == null) {
            writeByte(0);
        } else {
            new NBTOutputStream(this).writeTag(tag, Tag.DEFAULT_MAX_DEPTH);
        }
    }

    public byte[] toByteArray() {
        return outputBuffer.toByteArray();
    }
}
