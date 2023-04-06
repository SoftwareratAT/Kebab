package org.kebab.server;

import de.leonhard.storage.Toml;
import org.kebab.common.KebabRegistry;

import java.util.Map;
import java.util.Optional;

public final class KebabConfiguration {
    private final Map<String, Object> mapping;
    private final String host;
    private final int port;
    private final short playerTps;
    private final short worldTps;
    private final int viewDistance;
    private final int simulationDistance;

    public KebabConfiguration(Toml configuration) {
        KebabRegistry.register(this);
        this.mapping = configuration.getData();
        this.host = getString("server.host").orElse("0.0.0.0");
        this.port = getInt("server.port").orElse(25565);
        this.playerTps = getShort("scheduler.playerTps").orElse((short) 20);
        this.worldTps = getShort("scheduler.worldTps").orElse((short) 20);
        this.viewDistance = getInt("rendering.viewdistance").orElse(6);
        this.simulationDistance = getInt("rendering.simulationdistance").orElse(5);
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public short getWorldTps() {
        return this.worldTps;
    }

    public short getPlayerTps() {
        return this.playerTps;
    }

    public int getViewDistance() {
        return viewDistance;
    }

    public int getSimulationDistance() {
        return simulationDistance;
    }

    public Optional<String> getString(String key) {
        return Optional.ofNullable((String) mapping.get(key));
    }

    public Optional<Integer> getInt(String key) {
        return Optional.ofNullable((Integer) mapping.get(key));
    }

    public Optional<Short> getShort(String key) {
        return Optional.ofNullable((Short) mapping.get(key));
    }
}
