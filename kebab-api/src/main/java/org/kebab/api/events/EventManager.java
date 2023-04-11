package org.kebab.api.events;

import org.kebab.api.plugins.Plugin;

import java.util.concurrent.Future;

/**
 * Used to register/unregister listeners and call events.
 */
public interface EventManager {
    void registerListener(Plugin plugin, Object listener);

    Future<Void> callEventAndForget(Event event);

    <T extends Event> Future<T> callEvent(T event);

    void unregisterListeners(Plugin plugin);
}
