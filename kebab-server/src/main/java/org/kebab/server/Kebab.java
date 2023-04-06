package org.kebab.server;

import de.leonhard.storage.Toml;

public final class Kebab {
    private static final KebabConfiguration CONFIGURATION;
    static {
        CONFIGURATION = new KebabConfiguration(new Toml("kebab.toml", "./", Kebab.class.getClassLoader().getResourceAsStream("config.toml")));
    }

    public static void main(String... arguments) {
        KebabServer kebabServer = new KebabServer(getHost(), getPort());
        Runtime.getRuntime().addShutdownHook(new Thread(kebabServer::terminate));
        kebabServer.start();
    }

    private static String getHost() {
        String host = System.getProperties().getProperty("server.host");
        if (host == null) return CONFIGURATION.getHost();
        return host;
    }

    private static int getPort() {
        try {
            int port = Integer.parseInt(System.getProperties().getProperty("server.port"));
            if (port == 0) return CONFIGURATION.getPort();
            return port;
        } catch (NumberFormatException exception) {
            return CONFIGURATION.getPort();
        }
    }

    public static KebabConfiguration getConfig() {
        return CONFIGURATION;
    }
}
