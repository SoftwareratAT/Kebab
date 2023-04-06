package org.kebab.server.scheduler;

import org.kebab.server.Kebab;
import org.kebab.server.KebabServer;
import org.kebab.server.world.KebabWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public final class KebabWorldTick extends Thread {
    private static final Logger LOGGER = LoggerFactory.getLogger(KebabWorldTick.class);

    private final KebabServer server;
    private final short tickingInterval;
    private final AtomicLong tick;
    KebabWorldTick(KebabServer server) {
        short tps = Kebab.getConfig().getWorldTps();
        this.server = server;
        this.tickingInterval = (short) Math.round(1000.0 / tps);
        this.tick = new AtomicLong(0);
        if (tps > 40) {
            LOGGER.error("TPS cannot be higher than 40! Shutting down...");
            System.exit(2);
        }
    }

    @Override
    public void run() {
        while (server.isRunning()) {
            long start = System.currentTimeMillis();
            tick.incrementAndGet();
            try {
                updateWorlds(server.getUnsafeWorlds());
            } catch (IOException exception) {
                LOGGER.error("Cannot run update for each world! Tick: " + getCurrentTick(), exception);
            }
            long end = System.currentTimeMillis();
            try {
                TimeUnit.MILLISECONDS.sleep(tickingInterval - (end - start));
            } catch (InterruptedException exception) {
                LOGGER.error("Cannot sleep Tick thread", exception);
            }
        }
    }

    private void updateWorlds(Collection<KebabWorld> worlds) throws IOException {
        for (KebabWorld world : worlds) {
            //TODO Update world
        }
    }

    public long getCurrentTick() {
        return this.tick.get();
    }
}
