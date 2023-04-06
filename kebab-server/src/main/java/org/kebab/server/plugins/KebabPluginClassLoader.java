package org.kebab.server.plugins;

import org.kebab.server.Kebab;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

public final class KebabPluginClassLoader extends URLClassLoader {

    public KebabPluginClassLoader(URL[] urls) {
        super(urls, Kebab.class.getClassLoader());
    }

    void addPath(Path path) {
        try {
            addURL(path.toUri().toURL());
        } catch (MalformedURLException exception) {
            throw new AssertionError(exception);
        }
    }
}
