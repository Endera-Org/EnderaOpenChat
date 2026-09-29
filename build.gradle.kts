import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    kotlin("jvm") version "2.4.20"
    kotlin("plugin.serialization") version "2.4.20"
}

group = "org.endera"
version = "1.2.0"

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven("https://nexus.scarsz.me/content/groups/public/")
    maven("https://repo.extendedclip.com/releases/")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("dev.folia:folia-api:1.20.4-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.12.3")
    compileOnly("com.discordsrv:discordsrv:1.30.5")
    compileOnly("com.github.Zrips:CMI-API:9.8.6.4")

    // Provided at runtime by the EnderaLib plugin, together with Kotlin and kotlinx libraries
    compileOnly("com.github.Endera-Org:EnderaLib:1.6.0")
}

tasks.processResources {
    inputs.property("version", rootProject.version)
    filesMatching("**plugin.yml") {
        expand("version" to rootProject.version)
    }
}

kotlin {
    compilerOptions {
        apiVersion.set(KotlinVersion.KOTLIN_2_2)
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

tasks.withType<JavaCompile> {
    targetCompatibility = "17"
}
