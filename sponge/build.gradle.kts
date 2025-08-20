import org.spongepowered.gradle.plugin.config.PluginLoaders
import org.spongepowered.plugin.metadata.model.PluginDependency

plugins {
    id("java")
    id("org.spongepowered.gradle.plugin") version "2.2.0"
}

repositories {
    mavenCentral()
    maven("https://repo.spongepowered.org/repository/maven-public/")
}

dependencies {
    compileOnly("org.spongepowered:spongeapi:8.0.0")
}

sponge {
    apiVersion("10.0.0")
    license("MIT")
    loader {
        name(PluginLoaders.JAVA_PLAIN)
        version("1.0.0")
    }
    plugin("aegis") {
        displayName("Aegis")
        entrypoint("toutouchien.aegis.AegisSponge")
        description("Anti Server Scanner plugin")
        contributor("Toutouchien") {
            description("Lead Developper")
        }
        dependency("spongeapi") {
            loadOrder(PluginDependency.LoadOrder.AFTER)
            optional(false)
        }
    }
}