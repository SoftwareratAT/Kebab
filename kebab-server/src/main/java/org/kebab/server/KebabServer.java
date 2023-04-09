package org.kebab.server;

import net.kyori.adventure.text.Component;
import org.kebab.api.Server;
import org.kebab.api.entity.Player;
import org.kebab.api.events.EventManager;
import org.kebab.api.events.server.ServerReadyEvent;
import org.kebab.api.packet.PacketManager;
import org.kebab.api.plugins.Plugin;
import org.kebab.api.plugins.PluginManager;
import org.kebab.api.scheduler.Scheduler;
import org.kebab.api.world.World;
import org.kebab.common.KebabRegistry;
import org.kebab.common.utils.ConsoleColor;
import org.kebab.server.entity.KebabPlayer;
import org.kebab.server.events.KebabEventManager;
import org.kebab.server.network.KebabPacketManager;
import org.kebab.server.network.ServerConnection;
import org.kebab.server.plugins.KebabPluginManager;
import org.kebab.server.scheduler.KebabScheduler;
import org.kebab.server.world.KebabWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public final class KebabServer implements Server {
    private static final Logger LOGGER = LoggerFactory.getLogger(KebabServer.class);

    private final AtomicBoolean running;
    private final KebabEventManager eventManager;
    private final KebabPluginManager pluginManager;
    private final KebabScheduler scheduler;
    private final KebabWorlds kebabWorlds;
    private final KebabPlayers kebabPlayers;
    private final KebabPermissions kebabPermissions;
    private final KebabPacketManager packetManager;
    private final ServerConnection serverConnection;
    KebabServer(String host, int port) {
        KebabRegistry.register(this);
        LOGGER.info("Initializing...");
        this.running = new AtomicBoolean(false);
        this.eventManager = new KebabEventManager();
        this.scheduler = new KebabScheduler(this);
        this.kebabWorlds = new KebabWorlds();
        this.kebabPlayers = new KebabPlayers();
        this.pluginManager = new KebabPluginManager(this);
        this.kebabPermissions = new KebabPermissions();
        this.packetManager = new KebabPacketManager();
        this.serverConnection = new ServerConnection(this, new InetSocketAddress(host, port));
    }

    void start() {
        if (this.running.get()) return;
        long start = System.currentTimeMillis();

        LOGGER.info(ConsoleColor.CYAN.apply("Loading plugins..."));
        startPlugins();

        LOGGER.info("Setting up permissions...");
        try {
            kebabPermissions.callPermissionSetupEvent().get(5, TimeUnit.SECONDS);
        } catch (Exception exception) {
            LOGGER.error("Cannot setup Permissions. Using defaults", exception);
        }

        LOGGER.info("Loading worlds...");
        this.kebabWorlds.loadWorlds();

        this.running.set(true);

        LOGGER.info("Starting Tick-threads...");
        this.scheduler.startTicks();

        LOGGER.info("Starting ServerSocket");
        this.serverConnection.start();

        ServerReadyEvent readyEvent = new ServerReadyEvent(this);
        this.eventManager.callEventAndForget(readyEvent);
        long timeInSeconds = System.currentTimeMillis() - start;
        LOGGER.info(ConsoleColor.GREEN.apply("Successfully started in " + timeInSeconds + "ms"));
    }

    private void startPlugins() {
        try {
            Method startPlugins = KebabPluginManager.class.getDeclaredMethod("startPlugins");
            startPlugins.setAccessible(true);
            startPlugins.invoke(this.pluginManager);
            startPlugins.setAccessible(false);
        } catch (Exception exception) {
            LOGGER.error("Cannot start plugins via Reflection method", exception);
        }
    }

    public boolean isRunning() {
        return this.running.get();
    }

    private void shutdown(Consumer<Player> shutdownAction, boolean exit) {
        if (!this.running.get()) return;
        LOGGER.info(ConsoleColor.RED.apply("Shutting down..."));
        for (KebabPlayer player : new ArrayList<>(this.kebabPlayers.getPlayers())) {
            shutdownAction.accept(player);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {}
        }
        LOGGER.info("Kicking players...");
        for (KebabPlayer player : new ArrayList<>(this.kebabPlayers.getPlayers())) {
            player.disconnect(Component.empty());
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {}
        }

        this.running.set(false);

        LOGGER.info("Closing ServerSocket...");
        this.serverConnection.stopSocket();

        LOGGER.info("Stopping plugins...");
        for (Plugin plugin : this.pluginManager.getPlugins()) {
            LOGGER.info("Stopping plugin " + plugin.getPluginName() + " version " + plugin.getVersion() + "...");
            plugin.stop();
        }

        LOGGER.info(ConsoleColor.GREEN.apply("Terminating... Goodbye!"));
        if (exit) System.exit(0);
    }

    void terminate() {
        shutdown(player -> player.disconnect(Component.empty()), false);
    }

    public Collection<KebabWorld> getUnsafeWorlds() {
        return this.kebabWorlds.getWorlds();
    }

    public Collection<KebabPlayer> getUnsafePlayers() {
        return this.kebabPlayers.getPlayers();
    }

    @Override
    public Collection<Player> getOnlinePlayers() {
        return new ArrayList<>(this.kebabPlayers.getPlayers());
    }

    @Override
    public Collection<World> getWorlds() {
        return new ArrayList<>(this.kebabWorlds.getWorlds());
    }

    @Override
    public void shutdown() {
        shutdown(Component.empty());
    }

    @Override
    public void shutdown(Component reason) {
        shutdown(player -> player.disconnect(reason));
    }

    @Override
    public void shutdown(Consumer<Player> shutdownAction) {
        shutdown(shutdownAction, true);
    }

    @Override
    public Optional<World> getWorld(String name) {
        return Optional.ofNullable(this.kebabWorlds.getWorld(name));
    }

    @Override
    public Optional<Player> getPlayer(String name) {
        return Optional.ofNullable(this.kebabPlayers.getPlayer(name));
    }

    @Override
    public Optional<Player> getPlayer(UUID uuid) {
        return Optional.ofNullable(this.kebabPlayers.getPlayer(uuid));
    }

    @Override
    public EventManager getEventManager() {
        return this.eventManager;
    }

    @Override
    public PluginManager getPluginManager() {
        return this.pluginManager;
    }

    @Override
    public Scheduler getScheduler() {
        return this.scheduler;
    }

    @Override
    public PacketManager getPacketManager() {
        return this.packetManager;
    }
}
