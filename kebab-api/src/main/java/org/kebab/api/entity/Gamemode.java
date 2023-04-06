package org.kebab.api.entity;

public enum Gamemode {
    SURVIVAL((byte) 0),
    CREATIVE((byte) 1),
    ADVENTURE((byte) 2),
    SPECTATOR((byte) 3);
    private final byte id;
    Gamemode(byte id) {
        this.id = id;
    }

    public byte getId() {
        return id;
    }
}
