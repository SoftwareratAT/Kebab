package org.kebab.api.world;

import net.querz.mca.Chunk;

import java.util.Optional;

public final class ChunkPosition {
    private final World world;
    private final int x, z;

    public ChunkPosition(World world, int chunkX, int chunkZ) {
        if (world == null) throw new NullPointerException("World cannot be null");
        this.world = world;
        this.x = chunkX;
        this.z = chunkZ;
    }

    public ChunkPosition(Location location) {
        this(location.getWorld(), (int) location.getX() >> 4, (int) location.getZ() >> 4);
    }

    /*
    public ChunkPosition(World world, Chunk chunk) {
        this(world, world.getChunkX(chunk), world.getChunkZ(chunk));
    }
     */

    public World getWorld() {
        return world;
    }

    public int getChunkX() {
        return x;
    }

    public int getChunkZ() {
        return z;
    }

    public Optional<Chunk> getChunk() {
        return getWorld().getChunkAt(x, z);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((world == null) ? 0 : world.hashCode());
        result = prime * result + x;
        result = prime * result + z;
        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;
        if (getClass() != object.getClass()) return false;
        ChunkPosition other = (ChunkPosition) object;
        if (world == null) {
            if (other.world != null) return false;
        } else if (!world.equals(other.world)) return false;
        if (x != other.x) return false;
        return z == other.z;
    }
}
