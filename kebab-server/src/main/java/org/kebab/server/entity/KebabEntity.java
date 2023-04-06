package org.kebab.server.entity;

import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.kebab.api.entity.Entity;
import org.kebab.api.entity.EntityType;
import org.kebab.api.world.Location;
import org.kebab.api.world.World;
import org.kebab.server.world.KebabWorld;

import java.util.Optional;
import java.util.UUID;

public abstract class KebabEntity implements Sound.Emitter, Entity {
    @KebabDataWatcher.WatchableField(MetadataIndex = 0, WatchableObjectType = KebabDataWatcher.WatchableObjectType.BYTE, IsBitmask = true, Bitmask = 0x01)
    protected boolean onFire = false;
    @KebabDataWatcher.WatchableField(MetadataIndex = 0, WatchableObjectType = KebabDataWatcher.WatchableObjectType.BYTE, IsBitmask = true, Bitmask = 0x02)
    protected boolean crouching = false;
    @KebabDataWatcher.WatchableField(MetadataIndex = 0, WatchableObjectType = KebabDataWatcher.WatchableObjectType.BYTE, IsBitmask = true, Bitmask = 0x04)
    protected boolean unused = false;
    @KebabDataWatcher.WatchableField(MetadataIndex = 0, WatchableObjectType = KebabDataWatcher.WatchableObjectType.BYTE, IsBitmask = true, Bitmask = 0x08)
    protected boolean sprinting = false;
    @KebabDataWatcher.WatchableField(MetadataIndex = 0, WatchableObjectType = KebabDataWatcher.WatchableObjectType.BYTE, IsBitmask = true, Bitmask = 0x10)
    protected boolean swimming = false;
    @KebabDataWatcher.WatchableField(MetadataIndex = 0, WatchableObjectType = KebabDataWatcher.WatchableObjectType.BYTE, IsBitmask = true, Bitmask = 0x20)
    protected boolean invisible = false;
    @KebabDataWatcher.WatchableField(MetadataIndex = 0, WatchableObjectType = KebabDataWatcher.WatchableObjectType.BYTE, IsBitmask = true, Bitmask = 0x40)
    protected boolean glowing = false;
    @KebabDataWatcher.WatchableField(MetadataIndex = 0, WatchableObjectType = KebabDataWatcher.WatchableObjectType.BYTE, IsBitmask = true, Bitmask = 0x80)
    protected boolean elytraFlying = false;
    @KebabDataWatcher.WatchableField(MetadataIndex = 1, WatchableObjectType = KebabDataWatcher.WatchableObjectType.VARINT)
    protected int air = 300;
    @KebabDataWatcher.WatchableField(MetadataIndex = 2, WatchableObjectType = KebabDataWatcher.WatchableObjectType.CHAT, IsOptional = true)
    protected Component customName = null;
    @KebabDataWatcher.WatchableField(MetadataIndex = 3, WatchableObjectType = KebabDataWatcher.WatchableObjectType.BOOLEAN)
    protected boolean customNameVisible = false;
    @KebabDataWatcher.WatchableField(MetadataIndex = 4, WatchableObjectType = KebabDataWatcher.WatchableObjectType.BOOLEAN)
    protected boolean silent = false;
    @KebabDataWatcher.WatchableField(MetadataIndex = 5, WatchableObjectType = KebabDataWatcher.WatchableObjectType.BOOLEAN)
    protected boolean noGravity = false;
    @KebabDataWatcher.WatchableField(MetadataIndex = 7, WatchableObjectType = KebabDataWatcher.WatchableObjectType.VARINT)
    protected int frozenTicks = 0;

    protected final EntityType type;

    protected int entityId;
    protected UUID uuid;
    protected KebabWorld world;
    protected double x, y, z;
    protected float yaw, pitch;

    public KebabEntity(EntityType type, int entityId, UUID uuid, KebabWorld world, double x, double y, double z, float yaw, float pitch) {
        this.type = type;
        this.entityId = entityId;
        this.uuid = uuid;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    /*
    public DataWatcher getDataWatcher() {
        return world.getDataWatcher(this);
    }
     */

    public KebabEntity(EntityType type, UUID uuid, KebabWorld world, double x, double y, double z, float yaw, float pitch) {
        this(type, 0, uuid, world, x, y, z, yaw, pitch); //TODO Right entity id
    }

    public KebabEntity(EntityType type, KebabWorld world, double x, double y, double z, float yaw, float pitch) {
        this(type, 0, UUID.randomUUID(), world, x, y, z, yaw, pitch);
    }

    public KebabEntity(EntityType type, UUID uuid, Location location) {
        this(type, 0, uuid, (KebabWorld) location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    public KebabEntity(EntityType type, Location location) {
        this(type, 0, UUID.randomUUID(), (KebabWorld) location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    public EntityType getType() {
        return type;
    }

    public Location getLocation() {
        return new Location(world, x, y, z, yaw, pitch);
    }

    public void teleportUnsafe(Location location) {
        this.world = (KebabWorld) location.getWorld();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public Optional<Component> getCustomName() {
        return Optional.ofNullable(customName);
    }

    public void setCustomName(Component customName) {
        this.customName = customName;
    }

    public void setCustomName(ComponentLike customName) {
        this.customName = (Component) customName;
    }

    public boolean isOnFire() {
        return onFire;
    }

    public void setOnFire(boolean onFire) {
        this.onFire = onFire;
    }

    public boolean isCrouching() {
        return crouching;
    }

    public void setCrouching(boolean crouching) {
        this.crouching = crouching;
    }

    public boolean isSprinting() {
        return sprinting;
    }

    public void setSprinting(boolean sprinting) {
        this.sprinting = sprinting;
    }

    public boolean isSwimming() {
        return swimming;
    }

    public void setSwimming(boolean swimming) {
        this.swimming = swimming;
    }

    public boolean isInvisible() {
        return invisible;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    public boolean isGlowing() {
        return glowing;
    }

    public void setGlowing(boolean glowing) {
        this.glowing = glowing;
    }

    public boolean isElytraFlying() {
        return elytraFlying;
    }

    public void setElytraFlying(boolean elytraFlying) {
        this.elytraFlying = elytraFlying;
    }

    public int getAir() {
        return air;
    }

    public void setAir(int air) {
        this.air = air;
    }

    public boolean isCustomNameVisible() {
        return customNameVisible;
    }

    public void setCustomNameVisible(boolean customNameVisible) {
        this.customNameVisible = customNameVisible;
    }

    public boolean isSilent() {
        return silent;
    }

    public void setSilent(boolean silent) {
        this.silent = silent;
    }

    public boolean hasGravity() {
        return !noGravity;
    }

    public void setGravity(boolean gravity) {
        this.noGravity = !gravity;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = (KebabWorld) world;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public int getEntityId() {
        return entityId;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public boolean isValid() {
        return world.getEntities().contains(this);
    }
}
