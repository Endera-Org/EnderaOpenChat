import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    kotlin("jvm") version "2.2.0"
    kotlin("plugin.serialization") version "2.2.0" apply true
}

group = "org.endera"
version = "1.1.4"

repositories {
    mavenCentral()
//    mavenLocal()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven("https://nexus.scarsz.me/content/groups/public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("dev.folia:folia-api:1.20.4-R0.1-SNAPSHOT")
    compileOnly("com.discordsrv:discordsrv:1.29.0")
    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("net.kyori:adventure-text-minimessage:4.16.0")
    compileOnly("com.discordsrv:discordsrv:1.29.0")

    // Local Lib
//    implementation("org.endera.enderalib:enderalib:1.0-SNAPSHOT")
    implementation("com.github.Endera-Org:EnderaLib:1.4.5")
    implementation("com.github.Zrips:CMI-API:9.7.14.3")
}

tasks.processResources {
    inputs.property("version", rootProject.version)
    filesMatching("**plugin.yml") {
        expand("version" to rootProject.version)
    }
}

tasks.test {
    useJUnitPlatform()
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