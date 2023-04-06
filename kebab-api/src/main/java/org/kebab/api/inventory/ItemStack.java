package org.kebab.api.inventory;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.querz.nbt.tag.CompoundTag;

import java.util.Optional;

public interface ItemStack extends Cloneable {
    Key getMaterial();
    ItemStack clone();
    ItemStack getNewByType(Key material);
    int getAmount();
    ItemStack getNewByAmount(int amount);
    CompoundTag getNbt();
    ItemStack getNewByNbt(CompoundTag nbt);
    Optional<Component> getDisplayName();
    ItemStack setDisplayName(Component component);
    int getMaxStackSize();
    CompoundTag getFullTag();
    boolean isSimilar(ItemStack stack);
}
