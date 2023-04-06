package org.kebab.server.entity;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.kebab.api.entity.Player;

import java.util.UUID;

public final class KebabPlayer implements Player {
    private final UUID uuid;
    private final String name;

    public KebabPlayer(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    @Override
    public boolean hasPermission(String node) {
        return false;
    }

    @Override
    public void sendMessage(Component message) {

    }

    @Override
    public void sendMessage(ComponentLike message) {

    }

    @Override
    public double getX() {
        return 0;
    }

    @Override
    public double getY() {
        return 0;
    }

    @Override
    public double getZ() {
        return 0;
    }

    @Override
    public UUID getUUID() {
        return this.uuid;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void disconnect(Component reason) {

    }

    @Override
    public String toString() {
        return "[KebabPlayer:" + this.name + ":" + this.uuid + "]";
    }

    @Override
    public boolean equals(Player player) {
        return this.uuid.equals(player.getUUID());
    }
}
