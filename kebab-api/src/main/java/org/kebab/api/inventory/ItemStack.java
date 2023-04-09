package org.kebab.api.inventory;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.querz.nbt.tag.CompoundTag;
import org.kebab.api.KebabException;
import org.kebab.common.KebabRegistry;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public interface ItemStack extends Cloneable {
    ItemStack AIR = create(Key.key("minecraft:air"));

    static ItemStack create(Key material) {
        return create(material, 1);
    }

    static ItemStack create(Key material, int amount) {
        return create(material, amount, null);
    }

    static ItemStack create(Key material, int amount, CompoundTag nbt) {
        if (material == null) throw new NullPointerException("Material cannot be null!");
        Optional<Constructor<?>> kebabItemStackConstructor = KebabRegistry.getKebabItemStackConstructor();
        if (kebabItemStackConstructor.isEmpty()) return null;
        try {
            return (ItemStack) KebabRegistry.getKebabItemStackConstructor().get().newInstance(material, amount, nbt);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException exception) {
            throw new KebabException("Cannot initialize ItemStack! Please inform a Kebab developer.", exception);
        }
    }

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
