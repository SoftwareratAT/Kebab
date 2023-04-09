package org.kebab.api.packet;

import java.util.Optional;

public interface PacketManager {

    void registerIngoingPacket(Class<? extends IngoingPacket> packet);

    void registerOutgoingPacket(Class<? extends OutgoingPacket> packet);

    Optional<Class<? extends OutgoingPacket>> getOutgoingPacketById(byte id, PacketType packetType);

    Optional<Class<? extends IngoingPacket>> getIngoingPacketById(byte id, PacketType packetType);
}
