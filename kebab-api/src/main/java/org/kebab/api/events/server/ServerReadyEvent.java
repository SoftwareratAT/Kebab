package org.kebab.api.events.server;

import org.kebab.api.Server;
import org.kebab.api.events.Event;

/**
 * Called when the server is ready after start.
 */
public final class ServerReadyEvent extends Event {
    private final Server server;

    public ServerReadyEvent(Server server) {
        this.server = server;
    }

    public Server getServer() {
        return server;
    }
}
