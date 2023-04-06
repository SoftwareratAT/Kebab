package org.kebab.common;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.kyori.adventure.key.Key;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("ResultOfMethodCallIgnored")
public final class KebabRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(KebabRegistry.class);

    public static final BlockEntityRegistry BLOCK_ENTITY_TYPE;
    public static final ItemRegistry ITEM_REGISTRY;
    public static final MenuRegistry MENU_REGISTRY;
    private static final Collection<Object> OBJECTS;
    private static final JSONParser JSON_PARSER;
    static {
        OBJECTS = new ArrayList<>();
        JSON_PARSER = new JSONParser();
        File cacheFile = new File("./cache");
        if (!cacheFile.exists()) {
            cacheFile.mkdir();
        }
        if (!cacheFile.isDirectory()) {
            cacheFile.delete();
            cacheFile.mkdir();
        }
        String name = "registries.json";
        File file = new File(cacheFile, name);
        if (!file.exists()) {
            try (InputStream inputStream = Class.forName("org.kebab.server.Kebab").getClassLoader().getResourceAsStream(name)) {
                if (inputStream == null) throw new NullPointerException("Registries.json not found in classpath");
                Files.copy(inputStream, file.toPath());
            } catch (Exception exception) {
                LOGGER.error("Cannot copy registries file to cache", exception);
            }
        }

        if (!file.exists()) {
            LOGGER.warn("Please inform a Kebab developer about this error!");
            LOGGER.info("Shutting down...");
            System.exit(4);
        }

        Map<Key, Integer> blockEntityType = new HashMap<>();
        Key defaultItemKey = null;
        BiMap<Key, Integer> itemIds = HashBiMap.create();
        Map<Key, Integer> menuIds = new HashMap<>();
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            JSONObject json;
            json = (JSONObject) JSON_PARSER.parse(reader);

            JSONObject blockEntityJson = (JSONObject) ((JSONObject) json.get("minecraft:block_entity_type")).get("entries");
            for (Object obj : blockEntityJson.keySet()) {
                String key;
                key = obj.toString();
                int id = ((Number) ((JSONObject) blockEntityJson.get(key)).get("protocol_id")).intValue();
                blockEntityType.put(Key.key(key), id);
            }

            JSONObject itemJson = (JSONObject) json.get("minecraft:item");
            defaultItemKey = Key.key((String) itemJson.get("default"));
            JSONObject itemEntriesJson = (JSONObject) itemJson.get("entries");
            for (Object obj : itemEntriesJson.keySet()) {
                String key = obj.toString();
                int id = ((Number) ((JSONObject) itemEntriesJson.get(key)).get("protocol_id")).intValue();
                itemIds.put(Key.key(key), id);
            }

            JSONObject menuEntriesJson = (JSONObject) ((JSONObject) json.get("minecraft:menu")).get("entries");
            for (Object obj : menuEntriesJson.keySet()) {
                String key = obj.toString();
                int id = ((Number) ((JSONObject) menuEntriesJson.get(key)).get("protocol_id")).intValue();
                menuIds.put(Key.key(key), id);
            }
        } catch (IOException | ParseException exception) {
            LOGGER.error("Cannot read registries.json from cache", exception);
            LOGGER.info("Deleting registries.json for renew.");
            file.delete();
            LOGGER.info("Shutting down...");
            System.exit(4);
        }
        BLOCK_ENTITY_TYPE = new BlockEntityRegistry(blockEntityType);
        ITEM_REGISTRY = new ItemRegistry(defaultItemKey, itemIds);
        MENU_REGISTRY = new MenuRegistry(menuIds);
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<T> get(Class<T> clazz) {
        for (Object object : OBJECTS) {
            if (clazz.equals(object.getClass())) return Optional.of((T) object);
        }
        return Optional.empty();
    }

    public static void register(Object object) {
        OBJECTS.add(object);
    }

    public static class BlockEntityRegistry {

        private Map<Key, Integer> blockEntityType;

        private BlockEntityRegistry(Map<Key, Integer> blockEntityType) {
            this.blockEntityType = blockEntityType;
        }

        public int getId(Key key) {
            Integer exact = blockEntityType.get(key);
            if (exact != null) {
                return exact;
            }
            List<String> toTest = new LinkedList<>();
            toTest.add(key.value());
            if (key.value().contains("head")) {
                toTest.add("skull");
            }
            for (Map.Entry<Key, Integer> entry : blockEntityType.entrySet()) {
                Key Key = entry.getKey();
                for (String each : toTest) {
                    if (Key.namespace().equals(key.namespace()) && (each.contains(Key.value()) || Key.value().contains(each))) {
                        return entry.getValue();
                    }
                }
            }
            return -1;
        }
    }

    public static class ItemRegistry {

        private final Key defaultKey;
        private final BiMap<Key, Integer> itemIds;

        private ItemRegistry(Key defaultKey, BiMap<Key, Integer> itemIds) {
            this.defaultKey = defaultKey;
            this.itemIds = itemIds;
        }

        public Key getDefaultKey() {
            return defaultKey;
        }

        public int getId(Key key) {
            Integer id = itemIds.get(key);
            if (id != null) {
                return id;
            }
            if (defaultKey == null) {
                return 0;
            }
            return itemIds.getOrDefault(defaultKey, 0);
        }

        public Key fromId(int id) {
            return itemIds.inverse().getOrDefault(id, defaultKey);
        }
    }

    public static class MenuRegistry {

        private final Map<Key, Integer> menuIds;

        private MenuRegistry(Map<Key, Integer> menuIds) {
            this.menuIds = menuIds;
        }

        public int getId(Key key) {
            return menuIds.getOrDefault(key, -1);
        }
    }
}
