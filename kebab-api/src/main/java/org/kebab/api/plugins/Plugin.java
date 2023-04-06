package org.kebab.api.plugins;

import org.kebab.api.Server;
import org.kebab.api.events.EventManager;
import org.kebab.api.scheduler.Scheduler;
import org.kebab.common.plugin.PluginConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Represents a Kebab plugin.
 */
public abstract class Plugin {
    private Logger logger;
    private Server server;
    private PluginManager pluginManager;
    private EventManager eventManager;
    private Scheduler scheduler;
    private PluginConfiguration pluginConfiguration;

    public Plugin() {}

    private Plugin(Server server, PluginManager pluginManager, EventManager eventManager, Scheduler scheduler, PluginConfiguration configuration) {
        this.server = server;
        this.pluginManager = pluginManager;
        this.eventManager = eventManager;
        this.scheduler = scheduler;
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

    /**
     *
     */
    public final Server getServer() {
        return this.server;
    }
    public final PluginManager getPluginManager() {
        return this.pluginManager;
    }
    public final EventManager getEventManager() {
        return this.eventManager;
    }

    public final Scheduler getScheduler() {
        return this.scheduler;
    }

    PluginConfiguration getPluginConfiguration() {
        return this.pluginConfiguration;
    }
}
