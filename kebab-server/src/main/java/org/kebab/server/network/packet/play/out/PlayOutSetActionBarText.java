package org.kebab.server.network.packet.play.out;

import net.kyori.adventure.text.Component;
import org.kebab.api.packet.io.KebabOutputStream;
import org.kebab.api.packet.OutgoingPacket;
import org.kebab.api.packet.Packet;
import org.kebab.api.packet.PacketType;
import org.kebab.common.utils.ComponentSerializer;

import java.io.IOException;

@Packet(packetId = 0x46, packetType = PacketType.PLAY_OUT)
public final class PlayOutSetActionBarText extends OutgoingPacket {
    private final Component text;

    public PlayOutSetActionBarText(Component text) {
        this.text = text;
    }

    public Component getText() {
        return text;
    }

    @Override
    public void write(KebabOutputStream output) throws IOException {
        output.writeString(ComponentSerializer.toJsonString(this.text));
    }
}
