package org.kebab.api.packet;

import org.kebab.api.packet.io.KebabInputStream;

import java.io.IOException;

/**
 * Represents an ingoing Packet used in server.
 * Must be registered in server.
 * Needs class annotation Packet.
 */
public abstract class IngoingPacket {

    public IngoingPacket() {}

    public IngoingPacket(KebabInputStream input) throws IOException {}
}
