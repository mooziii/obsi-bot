package me.obsilabor.obsibot.config

import kotlinx.serialization.Serializable

@Serializable
data class MongoConfig(
    val host: String,
    val port: Int,
    val database: String,
    val user: String,
    val password: String
)