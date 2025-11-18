plugins {
    kotlin("jvm") version "2.3.0-Beta1"
    id("com.gradleup.shadow") version "8.3.0"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "cn.irina"
version = "0.0.1"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") {
        name = "spigotmc-repo"
    }
    
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.1-R0.1-SNAPSHOT")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation(kotlin("reflect"))
    
    implementation("com.github.Carleslc.Simple-YAML:Simple-Yaml:1.8.4")
    implementation("org.reflections:reflections:0.10.2")
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:5.7.0-alpha0")
    implementation("io.github.revxrsal:lamp.common:4.0.0-rc.13")
    implementation("io.github.revxrsal:lamp.bukkit:4.0.0-rc.13")
    implementation("io.github.revxrsal:lamp.brigadier:4.0.0-rc.13")
}

val targetJavaVersion = 17
kotlin {
    jvmToolchain(targetJavaVersion)
}

tasks {
    compileJava {
        options.compilerArgs.add("-parameters")
    }
    
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.add("-nowarn")
    }
    
    shadowJar {
        mergeServiceFiles()
        
        archiveVersion.set(version.toString())
    }
    
    jar {
        enabled = false
    }
    
    build {
        dependsOn("shadowJar")
    }
}
