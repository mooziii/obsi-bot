plugins {
    kotlin("jvm") version "1.6.21"
}

val javaVersion = 17

group = "me.obsilabor"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}

tasks {
    compileJava {
        options.release.set(javaVersion)
        options.encoding = "UTF-8"
    }
    compileKotlin {
        kotlinOptions.jvmTarget = javaVersion.toString()
    }
}