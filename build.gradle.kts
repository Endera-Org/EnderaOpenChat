import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    kotlin("jvm") version "2.1.10"
    kotlin("plugin.serialization") version "2.1.10" apply true
}

group = "org.endera"
version = "1.0.3"

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
    implementation("com.github.Turbovadim:EnderaLib:1.4.2")
}

tasks.jar {
    dependsOn(generatePluginYml)
}

tasks.processResources {
    dependsOn(generatePluginYml)
}

val generatePluginYml = tasks.create("generatePluginYml", Copy::class.java) {
    from("/src/main/configs/plugin.yml")
    into("/src/main/resources")
    expand(mapOf("projectVersion" to project.version))
    outputs.upToDateWhen { false }
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    compilerOptions {
        apiVersion.set(KotlinVersion.KOTLIN_2_1)
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

tasks.withType<JavaCompile> {
    targetCompatibility = "17"
}