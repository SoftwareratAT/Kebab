## Kebab-API
**Here is everything you need.**

This module contains the plugins api. You can use it everywhere to control `kebab-server`.

If you need specific features not included in this api yet, feel free to create a feature-request in the issues section.

***

### Gradle dependency
**Soon!**

### Maven dependency
**Soon!**

***

### Setup a kebab-plugin project

Every plugin needs a `plugin.yml`. **It must contain the plugins name, version and main class.**

*Example plugin.yml:*
```yaml
name: "SomePlugin"
version: "v0.0.1"
main: "org.example.someplugin.SomePluginMain"
```

Your main class should look something like this here:

*Example plugin main class:*
```java
package org.example.someplugin;

import org.kebab.api.plugins.Plugin;
import org.kebab.api.Server;
import org.kebab.api.events.EventManager;
import org.kebab.api.scheduler.Scheduler;
import org.slf4j.Logger;

public final class SomePlugin extends Plugin {
    
    // No constructors with parameters allowed!!!
    
    public void start() {
        // Called when the plugin starts
        
        // Every needed instance is available here in this main class.
        Server server = getServer();
        EventManager eventManager = getEventManager();
        Scheduler scheduler = getScheduler();
        Logger logger = getLogger();
    }
    
    public void stop() {
        // Called when the plugin stops
    }
}
```

***

### Setup a listener

In Kebab you have various events built-in. To listen for them, you will need an event-listener class.
**Kebab doesn't support lambda listeners!**

*Example listener class:*

```java
package org.example.someplugin;

import org.kebab.api.events.Listener;
import org.kebab.api.events.player.PlayerJoinEvent;
import org.kebab.api.entity.Player;
import net.kyori.adventure.text.Component;

public final class ExampleJoinListener {
    
    @Listener
    public void onPlayerJoinEvent(PlayerJoinEvent playerJoinEvent) {
        Player player = playerJoinEvent.getPlayer();
        player.sendMessage(Component.text("Hi there!"));
    }
}
```

To fully use this listener, you will have to register it in the kebab-eventmanager.

*Example listener registration in main plugin class:*

```java
package org.example.someplugin;

import org.kebab.api.plugins.Plugin;
import org.kebab.api.events.EventManager;

public final class SomePlugin extends Plugin {
    
    public void start() {
        EventManager eventManager = getEventManager();
        // Registers the listener when the plugin starts. Listener registration can be done at anytime.
        eventManager.registerListener(this, new ExampleJoinListener());
    }
    
    public void stop() {
        EventManager eventManager = getEventManager();
        // Unregister the listeners in case of reload. Not done automatically.
        eventManager.unregisterListeners(this);
    }
}
```