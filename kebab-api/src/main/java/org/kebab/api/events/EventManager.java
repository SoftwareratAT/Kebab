package org.kebab.api.events;

import org.kebab.api.plugins.Plugin;

import java.util.concurrent.CompletableFuture;

/**
 * Used to register/unregister listeners and call events.
 */
public interface EventManager {
    void registerListener(Plugin plugin, Object listener);

    CompletableFuture<Void> callEventAndForget(Event event);

    <T extends Event> CompletableFuture<T> callEvent(T event);

    void unregisterListeners(Plugin plugin);
}
