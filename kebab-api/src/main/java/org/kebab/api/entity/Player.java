package org.kebab.api.entity;

import net.kyori.adventure.text.Component;
import org.kebab.api.commands.CommandSource;

import java.util.UUID;

public interface Player extends LivingEntity, CommandSource {
    UUID getUUID();
    String getName();
    void disconnect(Component reason);
    boolean equals(Player player);
}
