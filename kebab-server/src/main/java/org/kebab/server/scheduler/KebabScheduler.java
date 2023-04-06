package org.kebab.server.scheduler;

import org.kebab.api.scheduler.KebabTask;
import org.kebab.api.scheduler.Scheduler;
import org.kebab.server.KebabServer;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

public final class KebabScheduler implements Scheduler {
    private final KebabWorldTick worldTick;
    private final KebabPlayerTick playerTick;
    public KebabScheduler(KebabServer server) {
        this.worldTick = new KebabWorldTick(server);
        this.playerTick = new KebabPlayerTick(server);
    }

    public void startTicks() {
        if (!this.playerTick.isAlive()) this.playerTick.start();
        if (!this.worldTick.isAlive()) this.worldTick.start();
    }

    @Override
    public long getCurrentWorldTick() {
        return this.worldTick.getCurrentTick();
    }

    @Override
    public long getCurrentPlayerTick() {
        return this.playerTick.getCurrentTick();
    }

    @Override
    public <T> KebabTask<T> runTask(Supplier<? extends T> action) {
        return new KebabAsyncTask<>(action);
    }

    @Override
    public <T> KebabTask<T> runTaskLater(Supplier<? extends T> action, long time, TimeUnit timeUnit) {
        return new KebabAsyncTask<>(action, time, timeUnit);
    }

    @Override
    public <T> KebabTask<T> runTaskTimer(Function<Integer, ? extends T> action, long time, TimeUnit timeUnit) {
        return new KebabAsyncTask<>(action, time, timeUnit);
    }
}
