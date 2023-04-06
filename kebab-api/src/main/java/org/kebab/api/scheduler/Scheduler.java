package org.kebab.api.scheduler;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Scheduler {
    long getCurrentWorldTick();

    long getCurrentPlayerTick();

    <T> KebabTask<T> runTask(Supplier<? extends T> action);

    <T> KebabTask<T> runTaskLater(Supplier<? extends T> action, long time, TimeUnit timeUnit);

    <T> KebabTask<T> runTaskTimer(Function<Integer, ? extends T> action, long time, TimeUnit timeUnit);
}
