package org.kebab.api.packet;

import org.kebab.api.packet.io.KebabOutputStream;

import java.io.IOException;

/**
 * Represents an outgoing Packet used by server.
 * Must be registered in server.
 * Needs class annotation Packet.
 */
public abstract class OutgoingPacket {
    public abstract void write(KebabOutputStream output) throws IOException;
}
