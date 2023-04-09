package org.kebab.server.events;

import org.kebab.api.events.Event;
import org.kebab.api.events.EventManager;
import org.kebab.api.plugins.Plugin;
import org.kebab.common.KebabRegistry;

import java.util.concurrent.CompletableFuture;

public class KebabEventManager implements EventManager {
    public KebabEventManager() {
        KebabRegistry.register(this);
    }

    @Override
    public void registerListener(Plugin plugin, Object listener) {

    }

    @Override
    public CompletableFuture<Void> callEventAndForget(Event event) {
        return null;
    }

    @Override
    public <T extends Event> CompletableFuture<T> callEvent(T event) {
        return null;
    }

    @Override
    public void unregisterListeners(Plugin plugin) {

    }
}
