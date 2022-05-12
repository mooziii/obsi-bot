plugins {
    kotlin("jvm") version "1.6.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

val javaVersion = 17

group = "me.obsilabor"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://maven.kotlindiscord.com/repository/maven-public/")
}

dependencies {
    // kotlin libraries
    implementation(kotlin("stdlib"))
    // kord and kordex libraries
    implementation("dev.kord:kord-core:0.8.0-M13")
    implementation("com.kotlindiscord.kord.extensions:kord-extensions:1.5.2-RC1")
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