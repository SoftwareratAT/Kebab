package org.kebab.server;

import org.kebab.server.world.KebabWorld;

import java.util.ArrayList;
import java.util.Collection;

public final class KebabWorlds {
    private final Collection<KebabWorld> worlds;
    KebabWorlds() {
        this.worlds = new ArrayList<>();
    }

    public void loadWorlds() {
        //TODO load all worlds in ./worlds/ here
    }

    public KebabWorld getWorld(String name) {
        for (KebabWorld world : new ArrayList<>(this.worlds)) {
            if (world.getName().equals(name)) return world;
        }
        return null;
    }

    public void registerWorld(KebabWorld world) {
        this.worlds.add(world);
    }

    public void unregisterWorld(KebabWorld world) {
        this.worlds.removeIf(world1 -> world1.getName().equals(world.getName()));
    }

    public Collection<KebabWorld> getWorlds() {
        return worlds;
    }
}
