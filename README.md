## Kebab Minecraft Server 1.19.4
**This project is still IN DEVELOPMENT and NOT STABLE!** Don't use it on Production networks yet.

Kebab is a lightweight backend Minecraft Java Edition server.
It's focus relies on maximum flexibility and performance.
The servers true logic comes from plugins. With them you can bring your server to a whole new level.
The API is very easy to use and gives you a lot of control over the server.
You can easily use this server with modified clients and build your own minecraft modifications. 
Kebab is completely asynchronous. Nothing is called from the main thread which makes the server relatively safe.
You won't need to create something like a packet-listener due to performance reasons even if possible.

Vanilla minecraft worlds are able to run on Kebab but are not supported or recommended.
**You should always use Schematics for worlds!** They are performant and way more lightweight.

***

### Plugin API
**[Available here!](/kebab-api)**

***

### Used libraries
| Library                                                               | Usecase                                                                               |
|-----------------------------------------------------------------------|---------------------------------------------------------------------------------------|
| [SimplixStorage](https://github.com/Simplix-Softworks/SimplixStorage) | A simple configuration-file library including independent Yaml, Toml and Json support |
| [Querz NBT](https://github.com/Querz/NBT)                             | An implementation of the Minecraft Java Edition NBT protocol                          |
| [Kyori Adventure](https://docs.advntr.dev/index.html)                 | Great libraries for serializing texts for Minecraft clients                           |

***

### Build Kebab

| Platform           | Build command       |
|--------------------|---------------------|
| Windows *(DOS)*    | ``gradlew build``   |
| Linux/Mac *(UNIX)* | ``./gradlew build`` |

**Your executable server *.jar* is located in `/build/libs`.**

The API is located in `/kebab-api/build/libs`.