package org.kebab.api.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;

public interface CommandSource {
    boolean hasPermission(String node);

    void sendMessage(Component message);

    void sendMessage(ComponentLike message);
}
