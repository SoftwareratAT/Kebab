package org.kebab.server.network.io;

import net.kyori.adventure.key.Key;
import net.querz.nbt.io.NBTInputStream;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.Tag;
import org.kebab.api.inventory.ItemStack;
import org.kebab.common.KebabRegistry;
import org.kebab.server.inventory.KebabItemStack;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.nio.charset.Charset;
import java.util.BitSet;
import java.util.EnumSet;
import java.util.UUID;

public final class KebabInputStream extends DataInputStream {
    public KebabInputStream(ByteArrayInputStream inputBuffer) {
        super(inputBuffer);
    }

    public ItemStack readItemStack() throws IOException {
        if (!readBoolean()) {
            return KebabItemStack.AIR;
        } else {
            Key key = KebabRegistry.ITEM_REGISTRY.fromId(readVarInt());
            byte amount = readByte();
            CompoundTag nbt = readCompoundTag();
            return new KebabItemStack(key, amount, nbt);
        }
    }

    public String readString(Charset charset) throws IOException {
        int length = readVarInt();

        if (length == -1) {
            throw new IOException("Premature end of stream.");
        }

        byte[] b = new byte[length];
        readFully(b);
        return new String(b, charset);
    }

    public <E extends Enum<E>> EnumSet<E> readEnumSet(Class<E> oclass) throws IOException {
        E[] ae = oclass.getEnumConstants();
        BitSet bitset = readFixedBitSet(ae.length);
        EnumSet<E> enumset = EnumSet.noneOf(oclass);
        for (int i = 0; i < ae.length; ++i) {
            if (bitset.get(i)) {
                enumset.add(ae[i]);
            }
        }
        return enumset;
    }

    public BitSet readFixedBitSet(int i) throws IOException {
        byte[] abyte = new byte[-Math.floorDiv(-i, 8)];
        readFully(abyte);
        return BitSet.valueOf(abyte);
    }

    public UUID readUUID() throws IOException {
        return new UUID(readLong(), readLong());
    }

    public CompoundTag readCompoundTag() throws IOException {
        byte b = readByte();
        if (b == 0) {
            return null;
        }
        PushbackInputStream buffered = new PushbackInputStream(this);
        buffered.unread(b);
        return (CompoundTag) new NBTInputStream(buffered).readTag(Tag.DEFAULT_MAX_DEPTH).getTag();
    }

    public int getStringLength(String string, Charset charset) throws IOException {
        byte[] bytes = string.getBytes(charset);
        return getVarIntLength(bytes.length) + bytes.length;
    }

    public int readVarInt() throws IOException {
        int numRead = 0;
        int result = 0;
        byte read;
        do {
            read = readByte();
            int value = (read & 0b01111111);
            result |= (value << (7 * numRead));

            numRead++;
            if (numRead > 5) {
                throw new RuntimeException("VarInt is too big");
            }
        } while ((read & 0b10000000) != 0);

        return result;
    }

    public long readVarLong() throws IOException {
        int numRead = 0;
        long result = 0;
        byte read;
        do {
            read = readByte();
            long value = (read & 0b01111111);
            result |= (value << (7 * numRead));

            numRead++;
            if (numRead > 10) {
                throw new RuntimeException("VarLong is too big");
            }
        } while ((read & 0b10000000) != 0);

        return result;
    }

    public static int getVarIntLength(int value) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(buffer);
        do {
            byte temp = (byte)(value & 0b01111111);
            // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
            value >>>= 7;
            if (value != 0) {
                temp |= 0b10000000;
            }
            out.writeByte(temp);
        } while (value != 0);
        return buffer.toByteArray().length;
    }
}
