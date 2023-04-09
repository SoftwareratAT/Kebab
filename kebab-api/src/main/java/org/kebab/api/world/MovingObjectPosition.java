package org.kebab.api.world;

import org.kebab.api.entity.Entity;

public abstract class MovingObjectPosition {
    protected final Vector location;

    protected MovingObjectPosition(Vector vector) {
        if (vector == null) throw new NullPointerException("Vector cannot be null");
        this.location = vector;
    }

    public double distanceTo(Entity entity) {
        if (entity == null) throw new NullPointerException("Entity cannot be null");
        double d0 = this.location.x - entity.getX();
        double d1 = this.location.y - entity.getY();
        double d2 = this.location.z - entity.getZ();
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public abstract MovingObjectPosition.MovingObjectType getType();

    public Vector getLocation() {
        return this.location;
    }


    public enum MovingObjectType {
        MISS,
        BLOCK,
        ENTITY
    }
}
