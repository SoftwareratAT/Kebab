package org.kebab.api.world;

public final class BlockPosition {
    private final int x, y, z;
    public BlockPosition(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Deprecated
    public static BlockPosition from(Location location) {
        if (location == null) throw new NullPointerException("Location cannot be null");
        return new BlockPosition((int) Math.floor(location.getX()), (int) Math.floor(location.getY()), (int) Math.floor(location.getZ()));
    }
}
