package org.kebab.api.plugins;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface PluginManager {
    Collection<Plugin> getPlugins();

    Optional<Plugin> getPlugin(String name);

    Optional<Plugin> loadPlugin(String pluginFileName);

    CompletableFuture<Void> restartPlugin(String name);

    CompletableFuture<Void> startPlugin(String name);

    CompletableFuture<Void> stopPlugin(String name);
}
