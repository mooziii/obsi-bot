plugins {
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

val javaVersion = 17

group = "me.obsilabor"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://maven.kotlindiscord.com/repository/maven-public/")
}

kotlin.sourceSets.all {
    languageSettings.optIn("kotlin.RequiresOptIn")
}

dependencies {
    // kotlin libraries
    implementation(kotlin("stdlib"))
    // ktor libraries
    implementation("io.ktor:ktor-client-core:1.6.7")
    implementation("io.ktor:ktor-client-cio:1.6.7")
    // kord and kordex libraries
    implementation("dev.kord:kord-core:0.8.0-M13")
    implementation("com.kotlindiscord.kord.extensions:kord-extensions:1.5.2-RC1")
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
}