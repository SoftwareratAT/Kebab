package org.kebab.api.entity;

import net.kyori.adventure.text.Component;
import org.kebab.api.commands.CommandSource;
import org.kebab.api.packet.OutgoingPacket;

import java.io.IOException;
import java.util.UUID;

public interface Player extends LivingEntity, CommandSource {
    UUID getUUID();
    String getName();
    void disconnect(Component reason);
    boolean equals(Player player);
    void sendPacket(OutgoingPacket packet) throws IOException;
    void sendPluginMessage(String channel, byte[] message) throws IOException;
    void sendServerBrand(Component brand);
}
