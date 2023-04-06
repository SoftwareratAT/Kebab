plugins {
    `maven-publish`
}

java {
    withJavadocJar()
}

tasks {
    jar {
        manifest {
            attributes["Automatic-Module-Name"] = "org.kebab.api"
        }
    }
}

val googleGuavaVersion: String by project.extra

dependencies {
    api("com.google.guava:guava:${googleGuavaVersion}")

    compileOnly(project(":kebab-common"))
}