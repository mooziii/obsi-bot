package me.obsilabor.obsibot.database

import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import com.mongodb.client.MongoCollection
import me.obsilabor.obsibot.config.ConfigManager
import me.obsilabor.obsibot.data.ObsiGuild
import org.bson.UuidRepresentation
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection

object MongoManager {

    val mongoClient = KMongo.createClient(settings = MongoClientSettings.builder()
        .uuidRepresentation(UuidRepresentation.STANDARD)
        .credential(
            MongoCredential.createCredential(
                ConfigManager.mongoConfig.user,
                ConfigManager.mongoConfig.database,
                ConfigManager.mongoConfig.password.toCharArray()
            ))
        .applyToClusterSettings { it.hosts(listOf(ServerAddress(ConfigManager.mongoConfig.host, ConfigManager.mongoConfig.port))) }
        .build()
    )
    val mongoDB = mongoClient.getDatabase(ConfigManager.mongoConfig.database)
    val guilds: MongoCollection<ObsiGuild>
    //val tickets: MongoCollection<Ticket>

    init {
        kotlin.runCatching {
            mongoDB.createCollection("obsiBotGuilds")
        }
        guilds = mongoDB.getCollection<ObsiGuild>("obsiBotGuilds")
        //tickets = mongoDB.getCollection<Ticket>("${collectionPrefix}_obsiBotTickets")
    }



}