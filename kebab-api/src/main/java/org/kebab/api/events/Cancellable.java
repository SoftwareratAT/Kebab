package org.kebab.api.events;

/**
 * Used in events that can be cancelled.
 */
public interface Cancellable {
    void setCancelled(boolean value);
    boolean isCancelled();
}
