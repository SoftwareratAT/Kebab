package org.kebab.server.network;

import org.kebab.server.KebabServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;

public final class ServerConnection extends Thread {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerConnection.class);

    private final KebabServer server;
    private ServerSocket serverSocket;
    private final Collection<ClientConnection> clientConnections;
    private final InetSocketAddress address;
    public ServerConnection(KebabServer server, InetSocketAddress address) {
        this.server = server;
        this.address = address;
        this.clientConnections = new ArrayList<>();
    }

    public ServerSocket getServerSocket() {
        return this.serverSocket;
    }

    public Collection<ClientConnection> getClients() {
        return this.clientConnections;
    }

    @Override
    public void run() {
        try {
            final int port = address.getPort();
            serverSocket = new ServerSocket(port, 50, address.getAddress());
            LOGGER.info("Starting connection on " + address.getHostName() + ":" + port);
            while (this.server.isRunning()) {
                Socket connection = serverSocket.accept();
                ClientConnection clientConnection = new ClientConnection(connection);
                clientConnections.add(clientConnection);
                clientConnection.start();
            }
            LOGGER.info("Closing connection...");
        } catch (Exception exception) {
            LOGGER.error("Error in ServerSocket", exception);
            start();
        }
    }

    public void stopSocket() {
        try {
            if (this.serverSocket != null) this.serverSocket.close();
        } catch (Exception exception) {
            LOGGER.error("Cannot close ServerSocket", exception);
        }
    }

    public boolean isRunning() {
        return this.serverSocket != null && !this.serverSocket.isClosed();
    }
}
