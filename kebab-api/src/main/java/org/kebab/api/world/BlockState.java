package org.kebab.api.world;

import net.kyori.adventure.key.Key;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.StringTag;
import net.querz.nbt.tag.Tag;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class BlockState {
    private final CompoundTag tag;

    public BlockState(CompoundTag tag) {
        if (tag == null) throw new NullPointerException("Tag cannot be null");
        this.tag = tag;
    }

    public CompoundTag toCompoundTag() {
        return tag;
    }

    public Key getType() {
        return Key.key(tag.getString("Name"));
    }

    public void setType(Key key) {
        if (key == null) throw new NullPointerException("Key cannot be null");
        tag.putString("Name", key.toString());
    }

    public Map<String, String> getProperties() {
        Map<String, String> mapping = new HashMap<>();
        for (Map.Entry<String, Tag<?>> entry : tag.getCompoundTag("Properties")) {
            String key = entry.getKey();
            String value = ((StringTag) entry.getValue()).getValue();
            mapping.put(key, value);
        }
        return mapping;
    }

    public Optional<String> getProperty(String key) {
        if (key == null) return Optional.empty();
        Tag<?> value = tag.getCompoundTag("Properties").get(key);
        return value == null ? Optional.empty() : Optional.of(((StringTag) value).getValue());
    }

    public void setProperties(Map<String, String> mapping) {
        if (mapping == null) throw new NullPointerException("Mapping cannot be null");
        CompoundTag properties = new CompoundTag();
        for (Map.Entry<String, String> entry : mapping.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            properties.putString(key, value);
        }
        tag.put("Properties", properties);
    }

    public <T> void setProperty(String key, T value) {
        if (key == null) throw new NullPointerException("Key cannot be null");
        CompoundTag properties = tag.getCompoundTag("Properties");
        properties.putString(key, ((T) value).toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockState that = (BlockState) o;
        return Objects.equals(tag, that.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag);
    }
}
