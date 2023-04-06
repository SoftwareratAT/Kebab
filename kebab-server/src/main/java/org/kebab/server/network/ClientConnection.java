package org.kebab.server.network;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.kebab.common.KebabRegistry;
import org.kebab.server.KebabServer;
import org.kebab.server.entity.KebabPlayer;
import org.kebab.server.network.channel.Channel;
import org.kebab.server.network.channel.ChannelPacketHandler;
import org.kebab.server.network.channel.ChannelPacketRead;
import org.kebab.server.network.io.KebabInputStream;
import org.kebab.server.network.io.KebabOutputStream;
import org.kebab.server.network.packet.IngoingPacket;
import org.kebab.server.network.packet.OutgoingPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

public class ClientConnection extends Thread {
    private static final Key DEFAULT_HANDLER_NAMESPACE = Key.key("default");
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientConnection.class);

    private final Socket clientSocket;
    //private final HandshakeHandler handleHandshake;
    //private final JoinHandler joinHandler;
    //private final PlayHandler playHandler;
    private Channel channel;
    private boolean running;
    private ClientState clientState;

    private KebabPlayer player;
    private TimerTask keepAliveTask;
    private AtomicLong lastPacketTimestamp;
    private AtomicLong lastKeepAlivePayload;
    private InetAddress inetAddress;
    private boolean ready;

    ClientConnection(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.inetAddress = clientSocket.getInetAddress();
        //this.handleHandshake = new HandshakeHandler(this);
        //this.joinHandler = new JoinHandler(this);
        //this.playHandler = new PlayHandler(this);
        this.lastPacketTimestamp = new AtomicLong(-1);
        this.lastKeepAlivePayload = new AtomicLong(-1);
        this.channel = null;
        this.running = false;
        this.ready = false;
    }

    public synchronized void sendPacket(OutgoingPacket packet) throws IOException {
        if (channel.writePacket(packet)) setLastPacketTimestamp(System.currentTimeMillis());
    }

    public synchronized void sendPluginMessage(String channel, byte[] message) throws IOException {
        //PlayOutPluginMessage playOutPluginMessage = new PlayOutPluginMessage(channel, message);
        //sendPacket(playOutPluginMessage);
    }

    @Override
    public void run() {
        running = true;
        clientState = ClientState.HANDSHAKE;
        try {
            //PlayerSkinProperty skinProperty = null;
            try {
                //skinProperty = handleHandshake();
            } catch (Exception exception) {
                //Give up if something fails
                channel.close();
                clientSocket.close();
                clientState = ClientState.DISCONNECTED;
            }
            //Handle play packets and events
            if (clientState == ClientState.PLAY) {
                //handleJoin(skinProperty);
                //handlePlay();
            }
        } catch (Exception ignored) {}
        //Handle disconnect when ClientSocket is not connected anymore
        try {
            channel.close();
            clientSocket.close();
        } catch (Exception ignored) {}

        this.clientState = ClientState.DISCONNECTED;
        KebabRegistry.get(KebabServer.class).ifPresent(server -> {
            //if (player != null) server.registerPlayer(player);
            //server.getServerConnection().getClients().remove(this);
            //server.unregisterPlayer(player.getUniqueId());
        });
        this.running = false;
    }

    public void disconnect(Component reason) {
        try {
            //PlayOutDisconnect playOutDisconnect = new PlayOutDisconnect(reason);
            //sendPacket(playOutDisconnect);
            sendPluginMessage("", new byte[0]);
        } catch (IOException exception) {
            LOGGER.error("Cannot disconnect client", exception);
        }
        close();
    }

    private void disconnectDuringLogin(Component reason) {
        try {
            //LoginOutDisconnect loginOutDisconnect = new LoginOutDisconnect(reason);
            //sendPacket(loginOutDisconnect);
            sendPluginMessage("", new byte[0]);
        } catch (IOException exception) {
            LOGGER.error("Cannot disconnect client during login", exception);
        }
        close();
    }

    void setChannel(KebabInputStream input, KebabOutputStream output) {
        this.channel = new Channel(input, output);

        this.channel.addHandlerBefore(DEFAULT_HANDLER_NAMESPACE, new ChannelPacketHandler() {
            @Override
            public ChannelPacketRead read(ChannelPacketRead read) {
                if (read.hasReadPacket()) {
                    return super.read(read);
                }
                try {
                    DataInput input = read.getDataInput();
                    int size = read.getSize();
                    byte packetId = read.getPacketId();
                    Class<? extends IngoingPacket> packetType = switch (clientState) {
                        case HANDSHAKE -> Protocol.getIngoingHandshakePackets().get(packetId);
                        case LOGIN -> Protocol.getIngoingLoginPackets().get(packetId);
                        case PLAY -> Protocol.getIngoingPlayPackets().get(packetId);
                        default -> throw new IllegalStateException("Illegal ClientState!");
                    };
                    if (packetType == null) {
                        input.skipBytes(size - KebabInputStream.getVarIntLength(packetId));
                        return null;
                    }
                    Constructor<?>[] constructors = packetType.getConstructors();
                    Constructor<?> constructor = Arrays.stream(constructors).filter(each -> each.getParameterCount() > 0 && each.getParameterTypes()[0].equals(DataInputStream.class)).findFirst().orElse(null);
                    if (constructor == null) {
                        throw new NoSuchMethodException(packetType + " has no valid constructors!");
                    } else if (constructor.getParameterCount() == 1) {
                        read.setPacket((IngoingPacket) constructor.newInstance(input));
                    } else if (constructor.getParameterCount() == 3) {
                        read.setPacket((IngoingPacket) constructor.newInstance(input, size, packetId));
                    } else {
                        throw new NoSuchMethodException(packetType + " has no valid constructors!");
                    }
                    return super.read(read);
                } catch (Exception e) {
                    throw new RuntimeException("Unable to read packet", e);
                }
            }
        });
    }

    private void close() {
        try {
            this.clientSocket.close();
        } catch (IOException exception) {
            LOGGER.error("Cannot close socket", exception);
        }
    }

    public void sendBrand(Component brand) throws IOException {
        KebabOutputStream output = new KebabOutputStream();
        output.writeString(LegacyComponentSerializer.legacySection().serialize(brand), StandardCharsets.UTF_8);
        sendPluginMessage("minecraft:brand", output.toByteArray());
    }

    public InetAddress getAddress() {
        return this.inetAddress;
    }

    public long getLastKeepAlivePayload() {
        return this.lastKeepAlivePayload.get();
    }

    public long getLastPacketTimestamp() {
        return this.lastPacketTimestamp.get();
    }

    public void setLastPacketTimestamp(long payLoad) {
        this.lastPacketTimestamp.set(payLoad);
    }

    public TimerTask getKeepAliveTask() {
        return this.keepAliveTask;
    }

    public KebabPlayer getPlayer() {
        return this.player;
    }

    public ClientState getClientState() {
        return this.clientState;
    }

    public Socket getSocket() {
        return this.clientSocket;
    }

    public Channel getChannel() {
        return this.channel;
    }

    public boolean isRunning() {
        return this.running;
    }

    public boolean isReady() {
        return this.ready;
    }

    Socket getClientSocket() {
        return this.clientSocket;
    }

    public enum ClientState {
        HANDSHAKE,
        LOGIN,
        PLAY,
        DISCONNECTED
    }
}
