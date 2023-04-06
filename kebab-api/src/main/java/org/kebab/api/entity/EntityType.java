package org.kebab.api.entity;

import net.kyori.adventure.key.Key;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public enum EntityType {
    ARMOR_STAND("armor_stand", ArmorStand.class, 1),
    DROPPED_ITEM("item", DroppedItem.class, 1, false),
    EXPERIENCE_ORB("experience_orb", null, 2),
    AREA_EFFECT_CLOUD("area_effect_cloud", null, 3),
    ELDER_GUARDIAN("elder_guardian", null, 4),
    WITHER_SKELETON("wither_skeleton", null, 5),
    STRAY("stray", null, 6),
    EGG("egg", null, 7),
    LEASH_HITCH("leash_knot", null, 8),
    PAINTING("painting", null, 9),
    ARROW("arrow", null, 10),
    SNOWBALL("snowball", null, 11),
    FIREBALL("fireball", null, 12),
    SMALL_FIREBALL("small_fireball", null, 13),
    PLAYER("player", Player.class, 106, false),
    UNKNOWN(null, null, -1, false);
    private final String name;
    private final Class<? extends Entity> clazz;
    private final short typeId;
    private final boolean independent;
    private final boolean living;
    private final Key key;
    private static final Map<String, EntityType> NAME_MAP = new HashMap<>();
    private static final Map<Short, EntityType> ID_MAP = new HashMap<>();

    static {
        for (EntityType type : values()) {
            if (type.name != null) NAME_MAP.put(type.name.toLowerCase(Locale.ENGLISH), type);
            if (type.typeId > 0) ID_MAP.put(type.typeId, type);
        }
    }

    EntityType(String name, Class<? extends Entity> clazz, int typeId) {
        this(name, clazz, typeId, true);
    }

    EntityType(String name, Class<? extends Entity> clazz, int typeId, boolean independent) {
        this.name = name;
        this.clazz = clazz;
        this.typeId = (short) typeId;
        this.independent = independent;
        this.living = clazz != null && LivingEntity.class.isAssignableFrom(clazz);
        this.key = (name == null) ? null : Key.key(Key.MINECRAFT_NAMESPACE, name);
    }

    @Deprecated
    public String getName() {
        return this.name;
    }

    public Key getKey() {
        return this.key;
    }

    private Class<? extends Entity> getEntityClass() {
        return this.clazz;
    }

    public short getTypeId() {
        return this.typeId;
    }

    public static Optional<EntityType> fromName(String name) {
        if (name == null) return Optional.empty();
        return Optional.ofNullable(NAME_MAP.get(name.toLowerCase(Locale.ENGLISH)));
    }

    public static Optional<EntityType> fromId(int id) {
        if (id > Short.MAX_VALUE) return Optional.empty();
        return Optional.ofNullable(ID_MAP.get((short) id));
    }

    public boolean isSpawnAble() {
        return independent;
    }

    public boolean isAlive() {
        return living;
    }
}
