package org.kebab.common.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public final class ComponentSerializer {

    public static String toLegacyString(Component component) {
        if (component == null) return "";
        return LegacyComponentSerializer.legacyAmpersand().serialize(component);
    }

    public static Component fromLegacyStringToComponent(String legacyString) {
        return LegacyComponentSerializer.legacySection().deserialize(legacyString);
    }

    public static String toJsonString(Component component) {
        if (component == null) return "";
        return GsonComponentSerializer.gson().serialize(component);
    }

    public static Component fromJsonStringToComponent(String jsonString) {
        return GsonComponentSerializer.gson().deserialize(jsonString);
    }
}
