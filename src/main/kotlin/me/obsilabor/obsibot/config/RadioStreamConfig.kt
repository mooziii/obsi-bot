package me.obsilabor.obsibot.config


@kotlinx.serialization.Serializable
data class RadioStreamConfig(
    val name: String,
    val url: String,
    val language: String,
    val flagEmoji: String
)
