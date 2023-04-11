package org.kebab.server.events;

import org.kebab.api.events.Event;
import org.kebab.api.events.EventManager;
import org.kebab.api.events.EventPriority;
import org.kebab.api.plugins.Plugin;
import org.kebab.common.KebabRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

public class KebabEventManager implements EventManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(KebabEventManager.class);
    private final Collection<ListenerPair> listeners;
    private final Map<Object, RegisteredListener> registeredListeners;
    public KebabEventManager() {
        KebabRegistry.register(this);
        this.listeners = new ArrayList<>();
        this.registeredListeners = new ConcurrentHashMap<>();
    }

    @Override
    public void registerListener(Plugin plugin, Object listener) {
        this.listeners.add(new ListenerPair(plugin, listener));
        this.registeredListeners.put(listener, new RegisteredListener(plugin, listener));
    }

    @Override
    public Future<Void> callEventAndForget(Event event) {
        return CompletableFuture.runAsync(() -> {
            callEvent0(event);
        });
    }

    @Override
    public <T extends Event> Future<T> callEvent(T event) {
        return CompletableFuture.supplyAsync(() -> {
            callEvent0(event);
            return event;
        });
    }

    private void callEvent0(Event event) {
        for (EventPriority priority : EventPriority.getInOrder()) {
            for (Map.Entry<Object, RegisteredListener> entry : this.registeredListeners.entrySet()) {
                for (Method method : entry.getValue().getListeners(event.getClass(), priority)) {
                    try {
                        method.invoke(entry.getKey(), event);
                    } catch (Exception exception) {
                        LOGGER.error("Error while passing " + event.getClass().getSimpleName() + " to plugin " + entry.getValue().getPlugin().getPluginName());
                    }
                }
            }
        }
    }

    @Override
    public void unregisterListeners(Plugin plugin) {
        this.listeners.removeIf(listenerPair -> {
            if (listenerPair.plugin.equals(plugin)) {
                registeredListeners.remove(listenerPair.listener);
                return true;
            }
            return false;
        });
    }

    private record ListenerPair(Plugin plugin, Object listener) {}
}
