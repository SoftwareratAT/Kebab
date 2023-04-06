package org.kebab.api.events;

import java.util.Optional;

/**
 * Used to set the priority of listeners. Lower gets called earlier than higher.
 */
public enum EventPriority {
    /**
     * Will be called first.
     */
    LOWEST((short) 0),
    /**
     * Will be called early.
     */
    LOW((short) 1),
    /**
     * The default priority.
     */
    NORMAL((short) 2),
    /**
     * Will be called late.
     */
    HIGH((short) 3),
    /**
     * Will be called at least.
     */
    HIGHEST((short) 4);
    private final short order;

    EventPriority(short order) {
        this.order = order;
    }

    public short getOrder() {
        return order;
    }

    public static Optional<EventPriority> getByOrder(short order) {
        for (EventPriority priority : EventPriority.values()) {
            if (priority.getOrder() == order) {
                return Optional.of(priority);
            }
        }
        return Optional.empty();
    }

    public static EventPriority[] getInOrder() {
        EventPriority[] eventPriorities = new EventPriority[EventPriority.values().length];
        for (short i = 0; i < eventPriorities.length; i++) {
            eventPriorities[i] = EventPriority.getByOrder(i).orElse(null);
        }
        return eventPriorities;
    }
}
