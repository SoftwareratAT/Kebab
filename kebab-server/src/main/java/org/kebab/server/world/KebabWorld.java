package org.kebab.server.world;

import net.querz.mca.Chunk;
import org.kebab.api.entity.Entity;
import org.kebab.api.entity.Player;
import org.kebab.api.world.BlockPosition;
import org.kebab.api.world.BlockState;
import org.kebab.api.world.Environment;
import org.kebab.api.world.World;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class KebabWorld implements World {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasSkyLight() {
        return false;
    }

    @Override
    public void setBlock(int x, int y, int z, BlockState blockState) {

    }

    @Override
    public void setBlock(BlockPosition position, BlockState blockState) {

    }

    @Override
    public BlockState getBlock(int x, int y, int z) {
        return null;
    }

    @Override
    public BlockState getBlock(BlockPosition position) {
        return null;
    }

    @Override
    public Optional<Chunk> getChunkAt(int x, int z) {
        return Optional.empty();
    }

    @Override
    public Optional<Chunk> getChunkAtWorldPos(int x, int z) {
        return Optional.empty();
    }

    @Override
    public int getChunkWidth() {
        return 0;
    }

    @Override
    public int getChunkLength() {
        return 0;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getLength() {
        return 0;
    }

    @Override
    public Collection<Player> getPlayers() {
        return null;
    }

    @Override
    public Set<Entity> getEntities() {
        return null;
    }

    @Override
    public Environment getEnvironment() {
        return null;
    }

    @Override
    public CompletableFuture<Void> save() {
        return null;
    }
}
