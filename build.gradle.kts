import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin
import com.github.jengelman.gradle.plugins.shadow.transformers.Log4j2PluginsCacheFileTransformer

plugins {
    `java-library`
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

allprojects {
    group = "org.kebab"
    version = "0.0.1"
}

val slf4jVersion: String by project.extra
val junitVersion: String by project.extra
val simplixStorageVersion: String by project.extra
val adventureVersion: String by project.extra
val querzNBTVersion: String by project.extra

subprojects {
    apply<JavaLibraryPlugin>()
    apply<ShadowPlugin>()

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(16))
        }
    }

    repositories {
        mavenCentral()
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/") // Adventure
        maven("https://jitpack.io") // A few other libraries
    }

    dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
        api("org.slf4j:slf4j-api:$slf4jVersion")
        implementation("com.github.simplix-softworks:simplixstorage:${simplixStorageVersion}")

        // Adventure
        implementation("net.kyori:adventure-api:${adventureVersion}")
        implementation("net.kyori:adventure-text-serializer-gson:${adventureVersion}")
        implementation("net.kyori:adventure-text-minimessage:${adventureVersion}")
        implementation("net.kyori:adventure-text-serializer-plain:${adventureVersion}")
        implementation("net.kyori:adventure-text-serializer-legacy:${adventureVersion}")
        implementation("net.kyori:adventure-nbt:${adventureVersion}")

        // Querz NBT
        implementation("com.github.Querz:NBT:${querzNBTVersion}")
    }

    tasks {
        test {
            useJUnitPlatform()
            reports {
                junitXml.required.set(true)
            }
        }

        shadowJar {
            transform(Log4j2PluginsCacheFileTransformer::class.java)
            relocate("de.leonhard.storage", "org.kebab.common.storage")
        }
    }
}