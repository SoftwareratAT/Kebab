package org.kebab.server.network;

import org.kebab.api.KebabException;
import org.kebab.api.packet.IngoingPacket;
import org.kebab.api.packet.OutgoingPacket;
import org.kebab.api.packet.Packet;
import org.kebab.api.packet.PacketManager;
import org.kebab.api.packet.PacketType;
import org.kebab.common.KebabRegistry;
import org.kebab.server.network.packet.play.in.PlayInChat;
import org.kebab.server.network.packet.play.out.PlayOutSetActionBarText;
import org.kebab.server.network.packet.play.out.PlayOutSystemChat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class KebabPacketManager implements PacketManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(KebabPacketManager.class);
    private static final Map<Byte, Class<? extends IngoingPacket>> INGOING_HANDSHAKE_PACKETS = new HashMap<>();
    private static final Map<Byte, Class<? extends OutgoingPacket>> OUTGOING_HANDSHAKE_PACKETS = new HashMap<>();
    private static final Map<Byte, Class<? extends IngoingPacket>> INGOING_LOGIN_PACKETS = new HashMap<>();
    private static final Map<Byte, Class<? extends OutgoingPacket>> OUTGOING_LOGIN_PACKETS = new HashMap<>();
    private static final Map<Byte, Class<? extends IngoingPacket>> INGOING_PLAY_PACKETS = new HashMap<>();
    private static final Map<Byte, Class<? extends OutgoingPacket>> OUTGOING_PLAY_PACKETS = new HashMap<>();
    static {
        LOGGER.info("Registering packets...");

        Collection<Class<? extends IngoingPacket>> ingoing = new ArrayList<>(Arrays.asList(
                PlayInChat.class
        ));
        Collection<Class<? extends OutgoingPacket>> outgoing = new ArrayList<>(Arrays.asList(
                PlayOutSystemChat.class,
                PlayOutSetActionBarText.class
        ));

        ingoing.forEach(KebabPacketManager::loadIngoing);
        outgoing.forEach(KebabPacketManager::loadOutgoing);
    }

    private static byte loadIngoing(Class<? extends IngoingPacket> clazz) {
        if (!clazz.isAnnotationPresent(Packet.class)) return 0;
        Packet packet = clazz.getAnnotation(Packet.class);
        byte packetId = packet.packetId();
        PacketType packetType = packet.packetType();
        LOGGER.debug("Loading IngoingPacket " + packetId + " " + clazz.getSimpleName());
        switch (packetType) {
            case HANDSHAKE_IN -> INGOING_HANDSHAKE_PACKETS.put(packetId, clazz);
            case PLAY_IN -> INGOING_PLAY_PACKETS.put(packetId, clazz);
            case LOGIN_IN -> INGOING_LOGIN_PACKETS.put(packetId, clazz);
        }
        return packetId;
    }

    private static byte loadOutgoing(Class<? extends OutgoingPacket> clazz) {
        if (!clazz.isAnnotationPresent(Packet.class)) return 0;
        Packet packet = clazz.getAnnotation(Packet.class);
        byte packetId = packet.packetId();
        PacketType packetType = packet.packetType();
        LOGGER.debug("Loading OutgoingPacket " + packetId + " " + clazz.getSimpleName());
        switch (packetType) {
            case HANDSHAKE_OUT -> OUTGOING_HANDSHAKE_PACKETS.put(packetId, clazz);
            case PLAY_OUT -> OUTGOING_PLAY_PACKETS.put(packetId, clazz);
            case LOGIN_OUT -> OUTGOING_LOGIN_PACKETS.put(packetId, clazz);
        }
        return packetId;
    }

    public static boolean isOutgoingValid(Class<? extends OutgoingPacket> packet) {
        if (getOutgoingHandshakePackets().containsValue(packet)) return true;
        if (getOutgoingLoginPackets().containsValue(packet)) return true;
        return getOutgoingPlayPackets().containsValue(packet);
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

    @Override
    public void registerIngoingPacket(Class<? extends IngoingPacket> packet) {
        if (packet == null) throw new NullPointerException("Packet cannot be null");
        if (!packet.isAnnotationPresent(Packet.class)) throw new KebabException("Missing Packet Annotation");
        byte packetId = loadIngoing(packet);
        LOGGER.info("Overriding Ingoing packet with id " + packetId + " with " + packet.getSimpleName());
    }

    @Override
    public void registerOutgoingPacket(Class<? extends OutgoingPacket> packet) {
        if (packet == null) throw new NullPointerException("Packet cannot be null");
        if (!packet.isAnnotationPresent(Packet.class)) throw new KebabException("Missing Packet Annotation");
        byte packetId = loadOutgoing(packet);
        LOGGER.info("Overriding Outgoing packet with id " + packetId + " with " + packet.getSimpleName());
    }

    @Override
    public Optional<Class<? extends OutgoingPacket>> getOutgoingPacketById(byte id, PacketType packetType) {
        return switch (packetType) {
            case PLAY_OUT -> Optional.ofNullable(OUTGOING_PLAY_PACKETS.get(id));
            case HANDSHAKE_OUT -> Optional.ofNullable(OUTGOING_HANDSHAKE_PACKETS.get(id));
            case LOGIN_OUT -> Optional.ofNullable(OUTGOING_LOGIN_PACKETS.get(id));
            default -> Optional.empty();
        };
    }

    @Override
    public Optional<Class<? extends IngoingPacket>> getIngoingPacketById(byte id, PacketType packetType) {
        return switch (packetType) {
            case PLAY_IN -> Optional.ofNullable(INGOING_PLAY_PACKETS.get(id));
            case HANDSHAKE_IN -> Optional.ofNullable(INGOING_HANDSHAKE_PACKETS.get(id));
            case LOGIN_IN -> Optional.ofNullable(INGOING_LOGIN_PACKETS.get(id));
            default -> Optional.empty();
        };
    }
}
