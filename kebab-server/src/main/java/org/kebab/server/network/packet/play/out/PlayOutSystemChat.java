package org.kebab.server.network.packet.play.out;

import net.kyori.adventure.text.Component;
import org.kebab.api.packet.io.KebabOutputStream;
import org.kebab.api.packet.OutgoingPacket;
import org.kebab.api.packet.Packet;
import org.kebab.api.packet.PacketType;
import org.kebab.common.utils.ComponentSerializer;

import java.io.IOException;

@Packet(packetId = 0x64, packetType = PacketType.PLAY_OUT)
public final class PlayOutSystemChat extends OutgoingPacket {
    private final Component message;
    private final boolean overlay;

    public PlayOutSystemChat(Component message, boolean overlay) {
        this.message = message;
        this.overlay = overlay;
    }

    public Component getMessage() {
        return message;
    }

    public boolean isOverlay() {
        return overlay;
    }

    @Override
    public void write(KebabOutputStream output) throws IOException {
        output.writeString(ComponentSerializer.toJsonString(this.message));
        output.writeBoolean(this.overlay);
    }
}
