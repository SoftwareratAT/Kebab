package org.kebab.server.network.packet;

import org.kebab.server.network.io.KebabOutputStream;

import java.io.IOException;

public interface OutgoingPacket {
    void write(KebabOutputStream output) throws IOException;
}
