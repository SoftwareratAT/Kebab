package org.kebab.server.entity;

import org.kebab.api.entity.EntityType;
import org.kebab.api.entity.LivingEntity;
import org.kebab.api.world.Location;
import org.kebab.server.world.KebabWorld;

import java.util.UUID;

public abstract class KebabLivingEntity extends KebabEntity implements LivingEntity {

    public KebabLivingEntity(EntityType type, int entityId, UUID uuid, KebabWorld world, double x, double y, double z, float yaw, float pitch) {
        super(type, entityId, uuid, world, x, y, z, yaw, pitch);
    }

    public KebabLivingEntity(EntityType type, UUID uuid, KebabWorld world, double x, double y, double z, float yaw, float pitch) {
        super(type, uuid, world, x, y, z, yaw, pitch);
    }

    public KebabLivingEntity(EntityType type, KebabWorld world, double x, double y, double z, float yaw, float pitch) {
        super(type, world, x, y, z, yaw, pitch);
    }

    public KebabLivingEntity(EntityType type, UUID uuid, Location location) {
        super(type, uuid, location);
    }

    public KebabLivingEntity(EntityType type, Location location) {
        super(type, location);
    }
}
