import net.axay.openapigenerator.OpenApiGenerateTask

plugins {
    kotlin("jvm") version "1.7.0"
    kotlin("plugin.serialization") version "1.6.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("net.axay.openapigenerator") version "0.1.2"
}

val javaVersion = 17
val kordexVersion = "1.5.2-RC1"

group = "me.obsilabor"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://maven.kotlindiscord.com/repository/maven-public/")
    maven("https://maven.fabricmc.net/")
    maven("https://maven.quiltmc.org/repository/release/")
    maven("https://jitpack.io")
    maven("https://m2.dv8tion.net/releases")
}

kotlin.sourceSets.all {
    languageSettings.optIn("kotlin.RequiresOptIn")
}

dependencies {
    // kotlin libraries
    implementation(kotlin("stdlib"))
    // kotlinx.coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.2")
    // ktor libraries
    implementation("io.ktor:ktor-client-core:1.6.7")
    implementation("io.ktor:ktor-client-cio:1.6.7")
    // kmongo
    implementation("org.litote.kmongo", "kmongo-core", "4.5.1")
    implementation("org.litote.kmongo", "kmongo-serialization-mapping", "4.5.1")
    // kord voice, lavaplayer and kordex libraries
    implementation("com.sedmelluq:lavaplayer:1.3.77")
    implementation("dev.kord:kord-voice:v0.8.0-M14")
    implementation("com.kotlindiscord.kord.extensions:kord-extensions:$kordexVersion")
    implementation("com.kotlindiscord.kord.extensions:extra-mappings:$kordexVersion")
    implementation("io.github.qbosst:kordex-hybrid-commands:1.0.4-SNAPSHOT")
    // utility libraries
    implementation("net.sf.jopt-simple:jopt-simple:5.0.4")
}

tasks {
    compileJava {
        options.release.set(javaVersion)
        options.encoding = "UTF-8"
    }
    compileKotlin {
        kotlinOptions.jvmTarget = javaVersion.toString()
    }
    shadowJar {
        manifest {
            attributes(
                "Main-Class" to "me.obsilabor.obsibot.ManagerKt"
            )
        }
    }
    register<OpenApiGenerateTask>("generateTruckersMP") {
        specUrl.set("https://raw.githubusercontent.com/TruckersMP/API-Documentation/main/OpenAPI-v2.yml")
        outputDirectory.set(file("src/main/kotlin/"))
        packageName.set("com.truckersmp.api")
    }
}