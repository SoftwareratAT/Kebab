package org.kebab.server.entity;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.kebab.api.KebabException;
import org.kebab.api.entity.Player;
import org.kebab.api.events.player.PlayerPluginMessageSendEvent;
import org.kebab.api.packet.OutgoingPacket;
import org.kebab.common.KebabRegistry;
import org.kebab.server.events.KebabEventManager;
import org.kebab.server.network.KebabPacketManager;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

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
        if (message == null) return;
    }

    @Override
    public void sendMessage(ComponentLike message) {
        sendMessage((Component) message);
    }

    @Override
    public void sendPacket(OutgoingPacket packet) throws IOException {
        if (!KebabPacketManager.isOutgoingValid(packet.getClass())) throw new KebabException("Packet is not registered");

    }

    @Override
    public void sendPluginMessage(String channel, byte[] message) throws IOException {
        if (channel == null) throw new NullPointerException("Channel cannot be null");
        PlayerPluginMessageSendEvent pluginMessageSendEvent = new PlayerPluginMessageSendEvent(this, channel, message);
        Optional<KebabEventManager> optionalKebabEventManager = KebabRegistry.get(KebabEventManager.class);
        if (optionalKebabEventManager.isPresent()) {
            KebabEventManager eventManager = optionalKebabEventManager.get();
            try {
                eventManager.callEventAndForget(pluginMessageSendEvent).get();
            } catch (InterruptedException | ExecutionException ignored) {
                pluginMessageSendEvent.setCancelled(true);
            }
        } else pluginMessageSendEvent.setCancelled(true);

        if (pluginMessageSendEvent.isCancelled()) return;

        //TODO send packet
    }

    @Override
    public void sendServerBrand(Component brand) {
        if (brand == null) brand = Component.text("Kebab");

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
        if (reason == null) reason = Component.empty();

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
