package me.obsilabor.obsibot.config

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import me.obsilabor.obsibot.ObsiBot
import java.io.File

object ConfigManager {

    val mongoConfig: MongoConfig
    get() {
        val file = File("config/mongodb.json")
        if(!file.exists()) {
            file.createNewFile()
            file.writeText(ObsiBot.json.encodeToString(MongoConfig("127.0.0.1", 27017, "database", "user", "password")))
        }
        return ObsiBot.json.decodeFromString(file.readText())
    }

    val radioConfig: List<RadioStreamConfig>
    get() {
        val file = File("config/radiostreams.json")
        return if(!file.exists()) {
            emptyList()
        } else {
            ObsiBot.json.decodeFromString(file.readText())
        }
    }
}