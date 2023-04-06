package org.kebab.api.inventory;

import org.kebab.api.entity.Entity;

import java.util.Optional;

public interface EntityInventory {
    void setItem(EquipmentSlot slot, ItemStack item);

    Optional<ItemStack> getItem(EquipmentSlot slot);

    Optional<ItemStack> getItemInMainHand();

    void setItemInMainHand(ItemStack item);

    Optional<ItemStack> getItemInOffHand();

    void setItemInOffHand(ItemStack item);

    Optional<ItemStack> getHelmet();

    void setHelmet(ItemStack helmet);

    Optional<ItemStack> getChestplate();

    void setChestplate(ItemStack chestplate);

    Optional<ItemStack> getLeggings();

    void setLeggings(ItemStack leggings);

    Optional<ItemStack> getBoots();

    void setBoots(ItemStack boots);

    ItemStack[] getArmorContents();

    void setArmorContents(ItemStack[] items);

    void clear();

    Entity getHolder();
}
