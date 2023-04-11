package org.kebab.common.utils;

import net.querz.mca.Chunk;
import net.querz.mca.LoadFlags;
import net.querz.mca.MCAUtil;
import net.querz.nbt.tag.CompoundTag;

import java.io.IOException;
import java.io.RandomAccessFile;

public final class KebabMCAFile {
    public static final int DEFAULT_DATA_VERSION = 1628;

    private int regionX, regionZ;
    private Chunk[] chunks;

    public KebabMCAFile(int regionX, int regionZ) {
        this.regionX = regionX;
        this.regionZ = regionZ;
    }

    public void deserialize(RandomAccessFile raf) throws IOException {
        deserialize(raf, LoadFlags.ALL_DATA);
    }

    public void deserialize(RandomAccessFile raf, long loadFlags) throws IOException {
        chunks = new Chunk[1024];
        for (int i = 0; i < 1024; i++) {
            raf.seek(i * 4);
            int offset = raf.read() << 16;
            offset |= (raf.read() & 0xFF) << 8;
            offset |= raf.read() & 0xFF;
            if (raf.readByte() == 0) {
                continue;
            }
            raf.seek(4096 + i * 4);
            int timestamp = raf.readInt();
            CompoundTag data = new CompoundTag();
            CompoundTag level = new CompoundTag();
            level.putInt("DataVersion", 3218);
            data.put("Level", level);
            Chunk chunk = new Chunk(data);
            raf.seek(4096 * offset + 4); //+4: skip data size
            //chunk.deserialize(raf, loadFlags);
            chunks[i] = chunk;
        }
    }

    public int serialize(RandomAccessFile raf) throws IOException {
        return serialize(raf, false);
    }

    public int serialize(RandomAccessFile raf, boolean changeLastUpdate) throws IOException {
        int globalOffset = 2;
        int lastWritten = 0;
        int timestamp = (int) (System.currentTimeMillis() / 1000L);
        int chunksWritten = 0;
        int chunkXOffset = MCAUtil.regionToChunk(regionX);
        int chunkZOffset = MCAUtil.regionToChunk(regionZ);

        if (chunks == null) {
            return 0;
        }

        for (int cx = 0; cx < 32; cx++) {
            for (int cz = 0; cz < 32; cz++) {
                int index = getChunkIndex(cx, cz);
                Chunk chunk = chunks[index];
                if (chunk == null) {
                    continue;
                }
                raf.seek(4096 * globalOffset);
                lastWritten = chunk.serialize(raf, chunkXOffset + cx, chunkZOffset + cz);

                if (lastWritten == 0) {
                    continue;
                }

                chunksWritten++;

                int sectors = (lastWritten >> 12) + (lastWritten % 4096 == 0 ? 0 : 1);

                raf.seek(index * 4);
                raf.writeByte(globalOffset >>> 16);
                raf.writeByte(globalOffset >> 8 & 0xFF);
                raf.writeByte(globalOffset & 0xFF);
                raf.writeByte(sectors);

                // write timestamp
                raf.seek(index * 4 + 4096);
                raf.writeInt(changeLastUpdate ? timestamp : chunk.getLastMCAUpdate());

                globalOffset += sectors;
            }
        }

        // padding
        if (lastWritten % 4096 != 0) {
            raf.seek(globalOffset * 4096 - 1);
            raf.write(0);
        }
        return chunksWritten;
    }

    public void setChunk(int index, Chunk chunk) {
        checkIndex(index);
        if (chunks == null) {
            chunks = new Chunk[1024];
        }
        chunks[index] = chunk;
    }

    public void setChunk(int chunkX, int chunkZ, Chunk chunk) {
        setChunk(getChunkIndex(chunkX, chunkZ), chunk);
    }

    public Chunk getChunk(int index) {
        checkIndex(index);
        if (chunks == null) {
            return null;
        }
        return chunks[index];
    }

    public Chunk getChunk(int chunkX, int chunkZ) {
        return getChunk(getChunkIndex(chunkX, chunkZ));
    }

    public static int getChunkIndex(int chunkX, int chunkZ) {
        return (chunkX & 0x1F) + (chunkZ & 0x1F) * 32;
    }

    private int checkIndex(int index) {
        if (index < 0 || index > 1023) {
            throw new IndexOutOfBoundsException();
        }
        return index;
    }

    private Chunk createChunkIfMissing(int blockX, int blockZ) {
        int chunkX = MCAUtil.blockToChunk(blockX), chunkZ = MCAUtil.blockToChunk(blockZ);
        Chunk chunk = getChunk(chunkX, chunkZ);
        if (chunk == null) {
            chunk = Chunk.newChunk();
            setChunk(getChunkIndex(chunkX, chunkZ), chunk);
        }
        return chunk;
    }

    public void setBiomeAt(int blockX, int blockY, int blockZ, int biomeID) {
        createChunkIfMissing(blockX, blockZ).setBiomeAt(blockX, blockY, blockZ, biomeID);
    }

    public int getBiomeAt(int blockX, int blockY, int blockZ) {
        int chunkX = MCAUtil.blockToChunk(blockX), chunkZ = MCAUtil.blockToChunk(blockZ);
        Chunk chunk = getChunk(getChunkIndex(chunkX, chunkZ));
        if (chunk == null) {
            return -1;
        }
        return chunk.getBiomeAt(blockX,blockY, blockZ);
    }

    public void setBlockStateAt(int blockX, int blockY, int blockZ, CompoundTag state, boolean cleanup) {
        createChunkIfMissing(blockX, blockZ).setBlockStateAt(blockX, blockY, blockZ, state, cleanup);
    }

    public CompoundTag getBlockStateAt(int blockX, int blockY, int blockZ) {
        int chunkX = MCAUtil.blockToChunk(blockX), chunkZ = MCAUtil.blockToChunk(blockZ);
        Chunk chunk = getChunk(chunkX, chunkZ);
        if (chunk == null) {
            return null;
        }
        return chunk.getBlockStateAt(blockX, blockY, blockZ);
    }

    public void cleanupPalettesAndBlockStates() {
        for (Chunk chunk : chunks) {
            if (chunk != null) {
                chunk.cleanupPalettesAndBlockStates();
            }
        }
    }
}
