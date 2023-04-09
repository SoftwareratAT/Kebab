package org.kebab.api.world;

public class MovingObjectPositionBlock extends MovingObjectPosition {
    private final BlockFace direction;
    private final BlockPosition blockPosition;
    private final boolean miss, inside;

    public static MovingObjectPosition miss(Vector vector, BlockFace direction, BlockPosition blockPosition) {
        if (vector == null) throw new NullPointerException("Vector cannot be null");
        if (direction == null) throw new NullPointerException("Direction cannot be null");
        if (blockPosition == null) throw new NullPointerException("BlockPosition cannot be null");
        return new MovingObjectPositionBlock(true, vector, direction, blockPosition, false);
    }

    public MovingObjectPositionBlock(Vector vector, BlockFace direction, BlockPosition blockPosition, boolean flag) {
        this(false, vector, direction, blockPosition, flag);
    }

    private MovingObjectPositionBlock(boolean flag, Vector vector, BlockFace direction, BlockPosition blockPosition, boolean flag1) {
        super(vector);
        this.miss = flag;
        this.direction = direction;
        this.blockPosition = blockPosition;
        this.inside = flag1;
    }

    public MovingObjectPositionBlock withDirection(BlockFace direction) {
        if (direction == null) throw new NullPointerException("BlockFace cannot be null");
        return new MovingObjectPositionBlock(this.miss, this.location, direction, this.blockPosition, this.inside);
    }

    public MovingObjectPositionBlock withPosition(BlockPosition blockPosition) {
        if (blockPosition == null) throw new NullPointerException("BlockPosition cannot be null");
        return new MovingObjectPositionBlock(this.miss, this.location, this.direction, blockPosition, this.inside);
    }

    public BlockPosition getBlockPosition() {
        return this.blockPosition;
    }

    public BlockFace getDirection() {
        return this.direction;
    }

    @Override
    public MovingObjectType getType() {
        return this.miss ? MovingObjectType.MISS : MovingObjectType.BLOCK;
    }

    public boolean isInside() {
        return this.inside;
    }
}
