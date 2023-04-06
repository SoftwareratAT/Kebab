tasks {
    jar {
        manifest {
            attributes["Automatic-Module-Name"] = "org.kebab.common"
        }
    }
}

val jsonSimpleVersion: String by project.extra
val googleGuavaVersion: String by project.extra
val snakeYamlVersion: String by project.extra

dependencies {
    implementation("com.google.guava:guava:${googleGuavaVersion}")
    implementation("com.googlecode.json-simple:json-simple:${jsonSimpleVersion}")
    implementation("org.yaml:snakeyaml:${snakeYamlVersion}")
}