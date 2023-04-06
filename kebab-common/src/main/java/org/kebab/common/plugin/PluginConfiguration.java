package org.kebab.common.plugin;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

public final class PluginConfiguration {
    private Map<String, Object> mapping;
    private String header;
    private final String pluginName, version, main;
    public PluginConfiguration(InputStream input) {
        Yaml yml = new Yaml();
        this.mapping = yml.load(new InputStreamReader(input, StandardCharsets.UTF_8));
        this.pluginName = getString("name");
        this.version = getString("version");
        this.main = getString("main");
    }

    public String getString(String key) {
        String[] tree = key.split("\\.");
        String string = null;
        for (String s : tree) {
            string = (String) mapping.get(s);
        }
        return string;
    }

    public Optional<String> getPluginName() {
        return Optional.ofNullable(pluginName);
    }

    public Optional<String> getVersion() {
        return Optional.ofNullable(version);
    }

    public Optional<String> getMain() {
        return Optional.ofNullable(main);
    }
}
