package org.kebab.server.plugins;

import org.kebab.api.plugins.Plugin;
import org.kebab.api.plugins.PluginManager;
import org.kebab.common.plugin.PluginConfiguration;
import org.kebab.server.KebabServer;
import org.kebab.server.events.KebabEventManager;
import org.kebab.server.scheduler.KebabScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class KebabPluginManager implements PluginManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(KebabPluginManager.class);

    private final Map<String, Plugin> plugins;
    private final File pluginFolder;
    private final KebabPluginClassLoader pluginClassLoader;
    private final KebabServer server;
    private final KebabEventManager eventManager;
    private final KebabScheduler scheduler;
    public KebabPluginManager(KebabServer server, KebabEventManager eventManager, KebabScheduler scheduler) {
        this.pluginFolder = new File("./plugins");
        this.plugins = new HashMap<>();
        this.pluginClassLoader = new KebabPluginClassLoader(new URL[0]);
        this.server = server;
        this.eventManager = eventManager;
        this.scheduler = scheduler;
        if (!pluginFolder.exists()) pluginFolder.mkdir();
    }

    private void startPlugins() {
        if (!this.plugins.isEmpty()) return;
        File[] files = this.pluginFolder.listFiles();
        if (files == null) return;
        for (File file : files) {
            if (!file.isFile() && !file.getName().endsWith(".jar")) continue;
            loadPlugin(file.getName()).ifPresent(Plugin::start);
        }
    }

    @Override
    public synchronized Optional<Plugin> loadPlugin(String pluginFileName) {
        if (pluginFileName == null || pluginFileName.isEmpty()) return Optional.empty();
        File jarFile = new File(pluginFolder.getPath() + "/" + pluginFileName);
        if (!jarFile.isFile() || !jarFile.getName().endsWith(".jar")) return Optional.empty();
        try (ZipInputStream zip = new ZipInputStream(new FileInputStream(jarFile))) {
            while (true) {
                ZipEntry entry = zip.getNextEntry();
                if (entry == null) break;
                String zipName = entry.getName();
                if (zipName.endsWith("module.yml")) {
                    PluginConfiguration pluginConfiguration = new PluginConfiguration(zip);
                    Optional<String> name = pluginConfiguration.getPluginName();
                    Optional<String> version = pluginConfiguration.getVersion();
                    Optional<String> main = pluginConfiguration.getMain();
                    if (name.isEmpty()) {
                        LOGGER.error("Name in " + jarFile.getName() + " is null");
                        break;
                    }
                    if (version.isEmpty()) {
                        LOGGER.error("Version in " + jarFile.getName() + " is null");
                        break;
                    }
                    if (main.isEmpty()) {
                        LOGGER.error("Main in " + jarFile.getName() + " is null");
                        break;
                    }
                    if (plugins.containsKey(name.get())) {
                        LOGGER.warn("Ambiguous plugin name in " + jarFile.getName() + " with the plugin " + plugins.get(name.get()).getClass().getName());
                        break;
                    }

                    this.pluginClassLoader.addPath(jarFile.toPath());

                    Class<?> clazz = this.pluginClassLoader.loadClass(main.get());
                    Plugin plugin = (Plugin) clazz.getDeclaredConstructor().newInstance(this.server, this, this.eventManager, this.scheduler, pluginConfiguration);
                    this.plugins.put(plugin.getPluginName(), plugin);
                    return Optional.of(plugin);
                }
            }
        } catch (Exception exception) {
            LOGGER.error("Cannot load plugin " + jarFile.getName(), exception);
        }
        return Optional.empty();
    }

    @Override
    public CompletableFuture<Void> restartPlugin(String name) {
        return CompletableFuture.runAsync(() -> {
            Plugin plugin = this.plugins.get(name);
            if (plugin == null) return;
            LOGGER.info("Restarting plugin " + name + "...");
            plugin.stop();
            plugin.start();
        });
    }

    @Override
    public CompletableFuture<Void> startPlugin(String name) {
        return CompletableFuture.runAsync(() -> {
            Plugin plugin = this.plugins.get(name);
            if (plugin == null) return;
            LOGGER.info("Starting plugin " + name + "...");
            plugin.start();
        });
    }

    @Override
    public CompletableFuture<Void> stopPlugin(String name) {
        return CompletableFuture.runAsync(() -> {
            Plugin plugin = this.plugins.get(name);
            if (plugin == null) return;
            LOGGER.info("Stopping plugin " + name + "...");
            plugin.stop();
        });
    }

    @Override
    public Collection<Plugin> getPlugins() {
        return this.plugins.values();
    }

    @Override
    public Optional<Plugin> getPlugin(String name) {
        return Optional.ofNullable(this.plugins.get(name));
    }
}
