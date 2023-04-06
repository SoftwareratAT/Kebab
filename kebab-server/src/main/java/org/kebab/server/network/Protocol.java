package org.kebab.server.network;

import org.kebab.server.network.packet.IngoingPacket;
import org.kebab.server.network.packet.OutgoingPacket;
import org.kebab.server.network.packet.Packet;
import org.kebab.server.network.packet.PacketType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class Protocol {
    private static final Map<Byte, Class<? extends IngoingPacket>> INGOING_HANDSHAKE_PACKETS = new HashMap<>();
    private static final Map<Byte, Class<? extends OutgoingPacket>> OUTGOING_HANDSHAKE_PACKETS = new HashMap<>();
    private static final Map<Byte, Class<? extends IngoingPacket>> INGOING_LOGIN_PACKETS = new HashMap<>();
    private static final Map<Byte, Class<? extends OutgoingPacket>> OUTGOING_LOGIN_PACKETS = new HashMap<>();
    private static final Map<Byte, Class<? extends IngoingPacket>> INGOING_PLAY_PACKETS = new HashMap<>();
    private static final Map<Byte, Class<? extends OutgoingPacket>> OUTGOING_PLAY_PACKETS = new HashMap<>();
    static {

        Collection<Class<? extends IngoingPacket>> ingoing = new ArrayList<>(Arrays.asList(
                //TODO Register all ingoing packets
        ));
        Collection<Class<? extends OutgoingPacket>> outgoing = new ArrayList<>(Arrays.asList(
                //TODO Register all outgoing packets
        ));

        ingoing.forEach(Protocol::loadIngoing);
        outgoing.forEach(Protocol::loadOutgoing);
    }

    private static void loadIngoing(Class<? extends IngoingPacket> clazz) {
        if (!clazz.isAnnotationPresent(Packet.class)) return;
        Packet packet = clazz.getAnnotation(Packet.class);
        byte packetId = packet.packetId();
        PacketType packetType = packet.packetType();
        switch (packetType) {
            case HANDSHAKE_IN -> INGOING_HANDSHAKE_PACKETS.put(packetId, clazz);
            case PLAY_IN -> INGOING_PLAY_PACKETS.put(packetId, clazz);
            case LOGIN_IN -> INGOING_LOGIN_PACKETS.put(packetId, clazz);
        }
    }

    private static void loadOutgoing(Class<? extends OutgoingPacket> clazz) {
        if (!clazz.isAnnotationPresent(Packet.class)) return;
        Packet packet = clazz.getAnnotation(Packet.class);
        byte packetId = packet.packetId();
        PacketType packetType = packet.packetType();
        switch (packetType) {
            case HANDSHAKE_OUT -> OUTGOING_HANDSHAKE_PACKETS.put(packetId, clazz);
            case PLAY_OUT -> OUTGOING_PLAY_PACKETS.put(packetId, clazz);
            case LOGIN_OUT -> OUTGOING_LOGIN_PACKETS.put(packetId, clazz);
        }
    }

    public static Map<Byte, Class<? extends IngoingPacket>> getIngoingHandshakePackets() {
        return INGOING_HANDSHAKE_PACKETS;
    }

    public static Map<Byte, Class<? extends IngoingPacket>> getIngoingLoginPackets() {
        return INGOING_LOGIN_PACKETS;
    }

    public static Map<Byte, Class<? extends IngoingPacket>> getIngoingPlayPackets() {
        return INGOING_PLAY_PACKETS;
    }

    public static Map<Byte, Class<? extends OutgoingPacket>> getOutgoingHandshakePackets() {
        return OUTGOING_HANDSHAKE_PACKETS;
    }

    public static Map<Byte, Class<? extends OutgoingPacket>> getOutgoingLoginPackets() {
        return OUTGOING_LOGIN_PACKETS;
    }

    public static Map<Byte, Class<? extends OutgoingPacket>> getOutgoingPlayPackets() {
        return OUTGOING_PLAY_PACKETS;
    }
}
