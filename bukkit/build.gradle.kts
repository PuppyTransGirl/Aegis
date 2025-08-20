import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")

    maven("https://repo.codemc.io/repository/maven-releases/")
    maven("https://repo.codemc.io/repository/maven-snapshots/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.1-R0.1-SNAPSHOT")
    implementation("com.github.retrooper:packetevents-spigot:2.9.4")
}

tasks.withType<ShadowJar> {
    relocate("com.github.retrooper.packetevents", "toutouchien.aegis.libs.com.github.retrooper.packetevents")
    relocate("io.github.retrooper.packetevents", "toutouchien.aegis.libs.io.github.retrooper.packetevents")
    relocate("net.kyori", "toutouchien.aegis.libs.net.kyori")
}