package org.kebab.api;

import net.kyori.adventure.text.Component;
import org.kebab.api.entity.Player;
import org.kebab.api.events.EventManager;
import org.kebab.api.packet.IngoingPacket;
import org.kebab.api.packet.PacketManager;
import org.kebab.api.plugins.PluginManager;
import org.kebab.api.scheduler.Scheduler;
import org.kebab.api.world.World;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public interface Server {
    Optional<World> getWorld(String name);

    Optional<Player> getPlayer(String name);

    Optional<Player> getPlayer(UUID uuid);

    Collection<Player> getOnlinePlayers();

    Collection<World> getWorlds();

    EventManager getEventManager();

    PluginManager getPluginManager();

    Scheduler getScheduler();

    PacketManager getPacketManager();

    void shutdown();

    void shutdown(Component reason);

    void shutdown(Consumer<Player> shutdownAction);
}
