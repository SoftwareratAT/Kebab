package org.kebab.server;

import de.leonhard.storage.Json;
import net.kyori.adventure.key.Key;
import net.querz.mca.Chunk;
import net.querz.mca.MCAFile;
import net.querz.mca.MCAUtil;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.CompoundTag;
import org.kebab.api.world.Environment;
import org.kebab.common.utils.KebabMCAFile;
import org.kebab.common.utils.KebabMCAUtil;
import org.kebab.server.world.KebabWorld;
import org.kebab.server.world.Schematic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public final class KebabWorlds {
    private static final Logger LOGGER = LoggerFactory.getLogger(KebabWorlds.class);

    private final File worldDirectory;
    private final File schematicDirectory;
    private final Collection<KebabWorld> worlds;
    KebabWorlds() {
        this.worlds = new ArrayList<>();
        this.worldDirectory = new File("./worlds");
        this.schematicDirectory = new File("./schematics");
        if (!this.worldDirectory.isDirectory()) {
            this.worldDirectory.delete();
        }
        if (!this.worldDirectory.exists()) {
            this.worldDirectory.mkdir();
        }
        if (!this.schematicDirectory.isDirectory()) {
            this.schematicDirectory.delete();
        }
        if (!this.schematicDirectory.exists()) {
            this.schematicDirectory.mkdir();
        }
    }

    public void loadSchematics() {
        File[] files = schematicDirectory.listFiles();
        if (files == null) return;
        long startTime = System.currentTimeMillis();
        for (File file : files) {
            if (!file.isDirectory()) return;
            File schematicFile = null;
            Json schematicConfig = null;
            File[] schematicFiles = file.listFiles();
            if (schematicFiles == null) continue;
            for (File file1 : schematicFiles) {
                if (file1.isFile()) {
                    if (file1.getName().endsWith(".schem")) {
                        schematicFile = file1;
                        continue;
                    }
                    if (file1.getName().endsWith(".json")) {
                        schematicConfig = new Json(file1);
                    }
                }
            }

            if (schematicFile == null) {
                LOGGER.warn("Schematic directory " + file.getName() + " doesn't have a .schem file");
                continue;
            }
            if (schematicConfig == null) {
                LOGGER.warn("Schematic directory " + file.getName() + " doesn't have a .json config");
                continue;
            }
            String worldName = schematicConfig.getString("name");
            if (worldName == null) {
                LOGGER.warn("World name in " + schematicConfig.getName() + " is empty");
                continue;
            }
            String environmentString = schematicConfig.getString("environment");
            if (environmentString == null) {
                LOGGER.warn("Environment in " + schematicConfig.getName() + " is empty");
                continue;
            }
            try {
                KebabWorld world = Schematic.toWorld(worldName, Environment.fromKey(Key.key(environmentString)).orElse(Environment.NORMAL), (CompoundTag) NBTUtil.read(schematicFile).getTag());
                this.worlds.add(world);
            } catch (Exception exception) {
                LOGGER.error("Cannot load schematic world " + worldName, exception);
            }
        }
        long time = (System.currentTimeMillis() - startTime);
        LOGGER.info("Loaded all schematics in " + time + "ms");
    }

    public void loadWorlds() {
        File[] files = worldDirectory.listFiles();
        if (files == null) return;
        long startTime = System.currentTimeMillis();
        for (File file : files) {
            if (!file.isDirectory()) continue;
            File worldConfigurationFile = new File(file.getPath() + "/world.json");
            if (!worldConfigurationFile.exists() || !worldConfigurationFile.isFile()) {
                LOGGER.warn("World " + file.getName() + " doesn't have a world.json innit. Please create one to load the world!");
                return;
            }
            Json worldConfiguration = new Json(worldConfigurationFile);
            String name = worldConfiguration.getString("name");
            if (name == null) {
                LOGGER.warn("World " + file.getName() + " doesn't have a name. Please add a name to the world.json of this world.");
                return;
            }
            if (getWorld(name) != null) {
                LOGGER.warn("Cannot load world " + file.getName() + "! The name in world.json is already used.");
                return;
            }
            int length = worldConfiguration.getInt("length");
            if (length == 0) {
                LOGGER.warn("Cannot load world " + name + " due to its missing length. Please add one.");
                return;
            }
            int width = worldConfiguration.getInt("width");
            if (width == 0) {
                LOGGER.warn("Cannot load world " + name + " due to its missing width. Please add one.");
                return;
            }
            String environmentString = worldConfiguration.getString("environment");
            Environment environment;
            if (environmentString != null) environment = Environment.fromKey(Key.key(environmentString)).orElse(Environment.NORMAL);
            else {
                LOGGER.warn("Environment of world " +  name + " not given. Using overworld.");
                environment = Environment.NORMAL;
            }

            LOGGER.info("Loading world " + name + "...");

            Chunk[][] chunks = new Chunk[(width >> 4) + 1][(length >> 4) + 1]; // This section eats a lot of ram if the length/width range is high
            try {
                // Goofy code start
                for (File mcaFile : Objects.requireNonNull(new File(file.getPath() + "/region").listFiles())) {
                    if (!mcaFile.isFile()) continue;
                    try {
                        KebabMCAFile mca = KebabMCAUtil.read(mcaFile); // Impossible to read via regions correctly
                        mca.cleanupPalettesAndBlockStates();
                        for (int x = 0; x < chunks.length; x++) {
                            for (int z = 0; z < chunks[x].length; z++) {
                                chunks[x][z] = mca.getChunk(x, z);
                                Chunk chunk = chunks[x][z];
                                if (chunk == null) continue;
                                chunk.cleanupPalettesAndBlockStates();
                            }
                        }
                    } catch (Exception exception) {
                        LOGGER.error("Cannot read mca file " + mcaFile.getName() + " for world " + name, exception);
                    }
                }
                // Goofy code end
            } catch (Exception exception) {
                LOGGER.error("Cannot read mca file of world " + name, exception);
            }
            KebabWorld world = new KebabWorld(name, environment, chunks, width, length);
            world.getBlockLightEngine().updateWorld();
            if (world.hasSkyLight()) world.getSkyLightEngine().updateWorld();
            this.worlds.add(world);
            LOGGER.info("Finished loading world " + name);
        }
        if (worlds.isEmpty()) {
            LOGGER.warn("No worlds found!");
            System.exit(0);
            return;
        }
        long time = (System.currentTimeMillis() - startTime);
        LOGGER.info("Loaded all worlds in " + time + "ms");
    }

    public KebabWorld getWorld(String name) {
        for (KebabWorld world : new ArrayList<>(this.worlds)) {
            if (world.getName().equals(name)) return world;
        }
        return null;
    }

    public void registerWorld(KebabWorld world) {
        this.worlds.add(world);
    }

    public void unregisterWorld(KebabWorld world) {
        this.worlds.removeIf(world1 -> world1.getName().equals(world.getName()));
    }

    public Collection<KebabWorld> getWorlds() {
        return worlds;
    }
}
