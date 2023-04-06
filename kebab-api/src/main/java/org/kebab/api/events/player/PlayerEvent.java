package org.kebab.api.events.player;

import org.kebab.api.entity.Player;
import org.kebab.api.events.Event;

/**
 * Used in every event a player is involved in.
 */
public abstract class PlayerEvent extends Event {
    private final Player player;
    public PlayerEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
