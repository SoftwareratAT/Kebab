package org.kebab.api.events.player;

import org.kebab.api.entity.Player;
import org.kebab.api.events.Cancellable;

public final class PlayerPluginMessageSendEvent extends PlayerEvent implements Cancellable {
    private boolean cancelled;
    private String channel;
    private byte[] message;
    public PlayerPluginMessageSendEvent(Player player, String channel, byte[] message) {
        super(player);
        this.channel = channel;
        this.message = message;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }

    public String getChannel() {
        return channel;
    }

    public byte[] getMessage() {
        return message;
    }

    @Override
    public void setCancelled(boolean value) {
        this.cancelled = value;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }
}
