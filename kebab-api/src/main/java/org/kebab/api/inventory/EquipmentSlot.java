package org.kebab.api.inventory;

public enum EquipmentSlot {
    MAIN_HAND,
    OFF_HAND,
    HELMET,
    CHEST_PLATE,
    LEGGINS,
    BOOTS;

    public boolean isHandSlot() {
        return this == MAIN_HAND || this == OFF_HAND;
    }

    public boolean isArmorSlot() {
        return !isHandSlot();
    }
}
