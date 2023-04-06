package org.kebab.server.inventory;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.querz.nbt.io.SNBTUtil;
import net.querz.nbt.tag.CompoundTag;
import org.kebab.api.inventory.ItemStack;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class KebabItemStack implements ItemStack {
    public static final ItemStack AIR = new KebabItemStack(Key.key("minecraft:air"));

    private final Key material;
    private final int amount;
    private final CompoundTag nbt;

    private CompoundTag fullTag;

    public KebabItemStack(Key material) {
        this(material, 1);
    }

    public KebabItemStack(Key material, int amount) {
        this(material, amount, null);
    }

    public KebabItemStack(Key material, int amount, CompoundTag nbt) {
        this.material = material;
        this.amount = amount;
        this.nbt = nbt;
        this.fullTag = null;
    }

    public KebabItemStack(CompoundTag fullTag) {
        this.material = Key.key(fullTag.getString("id"));
        this.amount = fullTag.getInt("Count");
        this.nbt = fullTag.containsKey("tag") ? fullTag.getCompoundTag("tag") : null;
        this.fullTag = fullTag.clone();
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public ItemStack clone() {
        return new KebabItemStack(material, amount, nbt == null ? null : nbt.clone());
    }

    @Override
    public Key getMaterial() {
        return material;
    }

    @Override
    public ItemStack getNewByType(Key material) {
        return new KebabItemStack(material, amount, nbt == null ? null : nbt.clone());
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public ItemStack getNewByAmount(int amount) {
        return new KebabItemStack(material, amount, nbt == null ? null : nbt.clone());
    }

    @Override
    public CompoundTag getNbt() {
        return nbt;
    }

    @Override
    public ItemStack getNewByNbt(CompoundTag nbt) {
        return new KebabItemStack(material, amount, nbt == null ? null : nbt.clone());
    }

    @Override
    public Optional<Component> getDisplayName() {
        if (getMaterial().equals(AIR.getMaterial()) || nbt == null) return Optional.empty();
        CompoundTag displayTag = nbt.getCompoundTag("display");
        if (displayTag == null) return Optional.empty();
        String json = displayTag.getString("Name");
        if (json == null) return Optional.empty();
        try {
            return Optional.of(GsonComponentSerializer.gson().deserialize(json));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public ItemStack setDisplayName(Component component) {
        if (getMaterial().equals(AIR.getMaterial())) return this;
        try {
            String json = GsonComponentSerializer.gson().serialize(component);
            CompoundTag nbt = this.nbt.clone();
            CompoundTag displayTag = nbt.getCompoundTag("display");
            if (displayTag == null) {
                nbt.put("display", displayTag = new CompoundTag());
            }
            displayTag.putString("Name", json);
            return getNewByNbt(nbt);
        } catch (Exception ignored) {}
        return this;
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public CompoundTag getFullTag() {
        if (fullTag != null) return fullTag;
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString("id", material.toString());
        compoundTag.putInt("Count", amount);
        if (nbt != null) compoundTag.put("tag", nbt);
        return fullTag = compoundTag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemStack itemStack = (ItemStack) o;
        return amount == itemStack.getAmount() && material.equals(itemStack.getMaterial()) && Objects.equals(nbt, itemStack.getNbt());
    }

    @Override
    public boolean isSimilar(ItemStack stack) {
        return stack != null && material.equals(stack.getMaterial()) && Objects.equals(nbt, stack.getNbt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(material, amount, nbt);
    }

    @Override
    public String toString() {
        try {
            return SNBTUtil.toSNBT(getFullTag());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return "";
    }
}
