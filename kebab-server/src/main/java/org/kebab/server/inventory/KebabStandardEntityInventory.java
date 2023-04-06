package org.kebab.server.inventory;

import org.kebab.api.entity.Entity;
import org.kebab.api.inventory.EntityInventory;
import org.kebab.api.inventory.EquipmentSlot;
import org.kebab.api.inventory.ItemStack;

import java.util.EnumMap;
import java.util.Optional;

public class KebabStandardEntityInventory implements EntityInventory {
    private final Entity entity;
    private final EnumMap<EquipmentSlot, ItemStack> itemStacks;

    public KebabStandardEntityInventory(Entity entity) {
        this.entity = entity;
        this.itemStacks = new EnumMap<>(EquipmentSlot.class);
    }

    @Override
    public void setItem(EquipmentSlot slot, ItemStack item) {
        this.itemStacks.put(slot, item);
    }

    @Override
    public Optional<ItemStack> getItem(EquipmentSlot slot) {
        return Optional.ofNullable(this.itemStacks.get(slot));
    }

    @Override
    public Optional<ItemStack> getItemInMainHand() {
        return getItem(EquipmentSlot.MAIN_HAND);
    }

    @Override
    public void setItemInMainHand(ItemStack item) {
        setItem(EquipmentSlot.MAIN_HAND, item);
    }

    @Override
    public Optional<ItemStack> getItemInOffHand() {
        return getItem(EquipmentSlot.OFF_HAND);
    }

    @Override
    public void setItemInOffHand(ItemStack item) {
        setItem(EquipmentSlot.OFF_HAND, item);
    }

    @Override
    public Optional<ItemStack> getHelmet() {
        return getItem(EquipmentSlot.HELMET);
    }

    @Override
    public void setHelmet(ItemStack helmet) {
        setItem(EquipmentSlot.HELMET, helmet);
    }

    @Override
    public Optional<ItemStack> getChestplate() {
        return getItem(EquipmentSlot.CHEST_PLATE);
    }

    @Override
    public void setChestplate(ItemStack chestplate) {
        setItem(EquipmentSlot.CHEST_PLATE, chestplate);
    }

    @Override
    public Optional<ItemStack> getLeggings() {
        return getItem(EquipmentSlot.LEGGINS);
    }

    @Override
    public void setLeggings(ItemStack leggings) {
        setItem(EquipmentSlot.LEGGINS, leggings);
    }

    @Override
    public Optional<ItemStack> getBoots() {
        return getItem(EquipmentSlot.BOOTS);
    }

    @Override
    public void setBoots(ItemStack boots) {
        setItem(EquipmentSlot.BOOTS, boots);
    }

    @Override
    public ItemStack[] getArmorContents() {
        return itemStacks.values().toArray(new ItemStack[0]);
    }

    @Override
    public void setArmorContents(ItemStack[] items) {
        if (items.length != 6) {
            throw new IllegalArgumentException("ItemStacks must have a length of 6");
        }
        EquipmentSlot[] equipmentSlots = EquipmentSlot.values();
        int i = 0;
        for (EquipmentSlot equipmentSlot : equipmentSlots) {
            setItem(equipmentSlot, items[i++]);
        }
    }

    @Override
    public void clear() {
        for (EquipmentSlot slot : this.itemStacks.keySet()) {
            setItem(slot, null);
        }
    }

    @Override
    public Entity getHolder() {
        return this.entity;
    }
}
