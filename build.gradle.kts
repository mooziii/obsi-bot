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
    //maven("https://oss.sonatype.org/content/repositories/snapshots")
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
    // kmongo
    implementation("org.litote.kmongo", "kmongo-core", "4.5.1")
    implementation("org.litote.kmongo", "kmongo-serialization-mapping", "4.5.1")
    // kord and kordex libraries
    //implementation("dev.kord:kord-core:0.8.0-M13-SNAPSHOT")
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