package me.obsilabor.obsibot.config

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

object ConfigManager {

    val mongoConfig: MongoConfig
    get() {
        val file = File("mongodb.json")
        if(!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
        if(!file.exists()) {
            file.createNewFile()
            file.writeText(Json.encodeToString(MongoConfig("127.0.0.1", 27017, "database", "user", "password")))
        }
        return Json.decodeFromString(file.readText())
    }
}