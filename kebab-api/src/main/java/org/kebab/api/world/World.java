package org.kebab.api.world;

import net.querz.mca.Chunk;
import org.kebab.api.entity.Entity;
import org.kebab.api.entity.Player;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface World {
    String getName();

    boolean hasSkyLight();

    void setBlock(int x, int y, int z, BlockState blockState);

    void setBlock(BlockPosition position, BlockState blockState);

    BlockState getBlock(int x, int y, int z);

    BlockState getBlock(BlockPosition position);

    Optional<Chunk> getChunkAt(int x, int z);

    Optional<Chunk> getChunkAtWorldPos(int x, int z);

    int getChunkWidth();

    int getChunkLength();

    int getWidth();

    int getLength();

    Collection<Player> getPlayers();

    Set<Entity> getEntities();

    Environment getEnvironment();

    CompletableFuture<Void> save();
}
