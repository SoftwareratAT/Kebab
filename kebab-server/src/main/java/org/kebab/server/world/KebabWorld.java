package org.kebab.server.world;

import net.querz.mca.Chunk;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import org.kebab.api.entity.Entity;
import org.kebab.api.entity.Player;
import org.kebab.api.world.BlockPosition;
import org.kebab.api.world.BlockState;
import org.kebab.api.world.Environment;
import org.kebab.api.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class KebabWorld implements World {
    private static final Logger LOGGER = LoggerFactory.getLogger(KebabWorld.class);

    public static final CompoundTag HEIGHT_MAP = new CompoundTag();
    public static final Chunk EMPTY_CHUNK = Chunk.newChunk();

    static {
        HEIGHT_MAP.putLongArray("MOTION_BLOCKING",
                new long[] {1371773531765642314L, 1389823183635651148L, 1371738278539598925L,
                        1389823183635388492L, 1353688558756731469L, 1389823114781694027L, 1317765589597723213L,
                        1371773531899860042L, 1389823183635651149L, 1371773462911685197L, 1389823183635650636L,
                        1353688626805119565L, 1371773531900123211L, 1335639250618849869L, 1371738278674077258L,
                        1389823114781694028L, 1353723811310638154L, 1371738278674077259L, 1335674228429068364L,
                        1335674228429067338L, 1335674228698027594L, 1317624576693539402L, 1335709481520370249L,
                        1299610178184057417L, 1335638906349064264L, 1299574993811968586L, 1299574924958011464L,
                        1299610178184056904L, 1299574924958011464L, 1299610109330100296L, 1299574924958011464L,
                        1299574924823793736L, 1299574924958011465L, 1281525273222484040L, 1299574924958011464L,
                        1281525273222484040L, 9548107335L});

        EMPTY_CHUNK.cleanupPalettesAndBlockStates();
        EMPTY_CHUNK.setHeightMaps(HEIGHT_MAP.clone());
        EMPTY_CHUNK.setBiomes(new int[256]);
        EMPTY_CHUNK.setTileEntities(new ListTag<>(CompoundTag.class));
    }

    private final String name;
    private final Environment environment;
    private final Chunk[][] chunks;
    private final int width;
    private final int length;
    private final BlockLightEngine blockLightEngine;
    private final SkyLightEngine skyLightEngine;
    //private final Map<Entity, DataWatcher> entities;
    public KebabWorld(String name, Environment environment, Chunk[][] chunks, int width, int length) {
        this.name = name;
        this.environment = environment;
        this.chunks = chunks;
        this.width = width;
        this.length = length;
        if (environment.hasSkyLight()) this.skyLightEngine = new SkyLightEngine(this);
        else this.skyLightEngine = null;
        this.blockLightEngine = new BlockLightEngine(this);
        //this.entities = new LinkedHashMap<>();
    }

    public KebabWorld(String name, Environment environment, int width, int length) {
        this.name = name;
        this.environment = environment;
        if (width != 0 && length != 0) {
            this.chunks = new Chunk[(width >> 4) + 1][(length >> 4) + 1];
            this.width = width;
            this.length = length;
        }
        else if (width != 0) {
            this.chunks = new Chunk[(width >> 4) + 1][(64000 >> 4) + 1];
            this.length = 64000;
            this.width = width;
        }
        else if (length != 0) {
            this.chunks = new Chunk[(64000 >> 4) + 1][(length >> 4) + 1];
            this.width = 64000;
            this.length = length;
        }
        else {
            this.chunks = new Chunk[(64000 >> 4) + 1][(64000 >> 4) + 1];
            this.width = 64000;
            this.length = 64000;
        }

        for (int x = 0; x < chunks.length; x++) {
            for (int z = 0; z < chunks[x].length; z++) {
                chunks[x][z] = Chunk.newChunk();
                Chunk chunk = chunks[x][z];
                chunk.cleanupPalettesAndBlockStates();
                chunk.setHeightMaps(HEIGHT_MAP.clone());
                chunk.setBiomes(new int[256]);
                chunk.setTileEntities(new ListTag<>(CompoundTag.class));
            }
        }
        this.blockLightEngine = new BlockLightEngine(this);
        if (environment.hasSkyLight()) this.skyLightEngine = new SkyLightEngine(this);
        else this.skyLightEngine = null;
        //this.entities = new LinkedHashMap<>();
    }

    public KebabWorld(String name, Environment environment) {
        this(name, environment, 0, 0);
    }

    public SkyLightEngine getSkyLightEngine() {
        return this.skyLightEngine;
    }

    public BlockLightEngine getBlockLightEngine() {
        return this.blockLightEngine;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasSkyLight() {
        return this.skyLightEngine != null;
    }

    @Override
    public void setBlock(int x, int y, int z, BlockState blockState) {
        Chunk chunk = this.chunks[(x >> 4)][(z >> 4)];
        if (chunk == null) {
            chunk = Chunk.newChunk();
            this.chunks[(x >> 4)][(z >> 4)] = chunk;
        }
        chunk.setBlockStateAt(x % 16, y % 16, z % 16, blockState.toCompoundTag(), false);
    }

    @Override
    public void setBlock(BlockPosition position, BlockState blockState) {
        setBlock(position.getX(), position.getY(), position.getZ(), blockState);
    }

    public void setBlock(int x, int y, int z, String blockData) {
        Chunk chunk = this.chunks[(x >> 4)][(z >> 4)];
        if (chunk == null) {
            chunk = Chunk.newChunk();
            this.chunks[(x >> 4)][(z >> 4)] = chunk;
        }
        CompoundTag block = SchematicConversion.toBlockTag(blockData);
        chunk.setBlockStateAt(x, y, z, block, false);
    }

    @Override
    public BlockState getBlock(int x, int y, int z) {
        Chunk chunk = this.chunks[(x >> 4)][(z >> 4)];
        if (chunk == null) {
            chunk = Chunk.newChunk();
            this.chunks[(x >> 4)][(x >> 4)] = chunk;
        }

        CompoundTag tag = chunk.getBlockStateAt(x, y, z);
        if (tag == null) {
            tag = new CompoundTag();
            tag.putString("Name", "minecraft:air");
        }
        return new BlockState(tag);
    }

    @Override
    public BlockState getBlock(BlockPosition position) {
        return getBlock(position.getX(), position.getY(), position.getZ());
    }

    @Override
    public Optional<Chunk> getChunkAt(int x, int z) {
        if (x < 0 || z < 0 || x >= chunks.length || z >= chunks[x].length) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.chunks[x][z]);
    }

    @Override
    public Optional<Chunk> getChunkAtWorldPos(int x, int z) {
        return Optional.ofNullable(this.chunks[(x >> 4)][(z >> 4)]);
    }

    @Override
    public int getChunkWidth() {
        return (width >> 4) + 1;
    }

    @Override
    public int getChunkLength() {
        return (length >> 4) + 1;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getLength() {
        return this.length;
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
        return this.environment;
    }

    @Override
    public CompletableFuture<Void> save() {
        return null;
    }

    public Chunk[][] getChunks() {
        return chunks;
    }
}
