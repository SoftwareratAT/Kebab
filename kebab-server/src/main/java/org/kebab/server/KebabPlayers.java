package org.kebab.server;

import org.kebab.server.entity.KebabPlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public final class KebabPlayers {
    private final Collection<KebabPlayer> players;
    KebabPlayers() {
        this.players = new ArrayList<>();
    }

    public KebabPlayer getPlayer(UUID uuid) {
        for (KebabPlayer player : new ArrayList<>(this.players)) {
            if (player.getUUID().equals(uuid)) return player;
        }
        return null;
    }

    public KebabPlayer getPlayer(String name) {
        for (KebabPlayer player : new ArrayList<>(this.players)) {
            if (player.getName().equals(name)) return player;
        }
        return null;
    }

    public void registerPlayer(KebabPlayer player) {
        this.players.add(player);
    }

    public void unregisterPlayer(KebabPlayer player) {
        this.players.removeIf(player1 -> player1.getUUID().equals(player.getUUID()));
    }

    public Collection<KebabPlayer> getPlayers() {
        return this.players;
    }
}
