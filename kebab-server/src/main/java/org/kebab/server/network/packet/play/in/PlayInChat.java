package org.kebab.server.network.packet.play.in;

import net.kyori.adventure.text.Component;
import org.kebab.api.packet.io.KebabInputStream;
import org.kebab.api.packet.IngoingPacket;
import org.kebab.api.packet.Packet;
import org.kebab.api.packet.PacketType;
import org.kebab.common.utils.ComponentSerializer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Packet(packetId = 0x00, packetType = PacketType.PLAY_IN)
public final class PlayInChat extends IngoingPacket {
    private final Component message;

    public PlayInChat(Component message) {
        this.message = message;
    }

    public PlayInChat(KebabInputStream input) throws IOException {
        this(ComponentSerializer.fromLegacyStringToComponent(input.readString(StandardCharsets.UTF_8)));
        input.readLong();
        input.readLong();
        input.readBoolean();
    }

    public Component getMessage() {
        return message;
    }
}
