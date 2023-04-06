plugins {
    application
}

application {
    mainClass.set("org.kebab.server.Kebab")
}

val kebabImplementationName: String by project.extra
val kebabVendor: String by project.extra
val kebabVersion: String by project.extra
val minecraftVersion: String by project.extra

tasks {
    jar {
        manifest {
            attributes["Implementation-Title"] = kebabImplementationName
            attributes["Implementation-Vendor"] = kebabVendor
            attributes["Implementation-Version"] = kebabVersion
            attributes["Minecraft-Version"] = minecraftVersion
            attributes["Multi-Release"] = true
        }
    }

    shadowJar {
        // Exclude useless things to reduce file size
        exclude("*.txt")
        exclude("*.xsd")
        exclude("*.dtd")
        exclude("*.properties")
        exclude("META-INF/maven/**")
        exclude("META-INF/NOTICE")
        exclude("META-INF/*.txt")
        exclude("META-INF/LICENSE")
        exclude("META-INF/DEPENDENCIES")
    }
}

val log4jVersion: String by project.extra
val snakeYamlVersion: String by project.extra

dependencies {
    implementation(project(":kebab-api"))
    implementation(project(":kebab-common"))

    // Log4j
    implementation("org.apache.logging.log4j:log4j-api:${log4jVersion}")
    implementation("org.apache.logging.log4j:log4j-core:${log4jVersion}")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:${log4jVersion}")
    implementation("org.apache.logging.log4j:log4j-iostreams:${log4jVersion}")
    implementation("org.apache.logging.log4j:log4j-jul:${log4jVersion}")

    implementation("org.yaml:snakeyaml:${snakeYamlVersion}")
}