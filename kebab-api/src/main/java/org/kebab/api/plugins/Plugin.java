package org.kebab.api.plugins;

import org.kebab.api.KebabException;
import org.kebab.api.Server;
import org.kebab.api.events.EventManager;
import org.kebab.api.packet.PacketManager;
import org.kebab.api.scheduler.Scheduler;
import org.kebab.common.plugin.PluginConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Represents a Kebab plugin.
 */
public abstract class Plugin {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private Server server;
    private PluginManager pluginManager;
    private EventManager eventManager;
    private Scheduler scheduler;
    private PacketManager packetManager;
    private PluginConfiguration pluginConfiguration;

    public Plugin() {}

    private Plugin(Server server, PluginConfiguration configuration) {
        this.server = server;
        this.pluginManager = server.getPluginManager();
        this.eventManager = server.getEventManager();
        this.scheduler = server.getScheduler();
        this.packetManager = server.getPacketManager();
        this.pluginConfiguration = configuration;
        Optional<String> pluginName = configuration.getPluginName();
        if (pluginName.isEmpty()) this.logger = LoggerFactory.getLogger(getClass());
        else this.logger = LoggerFactory.getLogger(pluginName.get());
    }

    public abstract void start();

    public abstract void stop();

    /**
     * Get this plugins name.
     */
    public final String getPluginName() {
        return this.pluginConfiguration.getPluginName().orElse(null);
    }

    /**
     * Get this plugins version.
     */
    public final String getVersion() {
        return this.pluginConfiguration.getVersion().orElse(null);
    }

    /**
     * Get this plugins logger.
     */
    public final Logger getLogger() {
        return this.logger;
    }

    public final Server getServer() {
        if (this.server == null) throw new KebabException("Server not initialized yet.");
        return this.server;
    }

    public final PluginManager getPluginManager() {
        if (this.server == null) throw new KebabException("PluginManager not initialized yet.");
        return this.pluginManager;
    }

    public final EventManager getEventManager() {
        if (this.server == null) throw new KebabException("EventManager not initialized yet.");
        return this.eventManager;
    }

    public final Scheduler getScheduler() {
        if (this.server == null) throw new KebabException("Scheduler not initialized yet.");
        return this.scheduler;
    }

    public final PacketManager getPacketManager() {
        if (this.server == null) throw new KebabException("PacketManager not initialized yet.");
        return this.packetManager;
    }

    final PluginConfiguration getPluginConfiguration() {
        return this.pluginConfiguration;
    }
}
