package org.kebab.api.events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks methods as listeners.
 * You need to provide one argument containing the event.
 * Don't forget to register this listeners class in EventManager.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Listener {
    EventPriority priority() default EventPriority.NORMAL;
}
