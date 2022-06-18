package me.obsilabor.obsibot.minecraft.modding

@kotlinx.serialization.Serializable
data class GameVersion(
    val version: String,
    val stable: Boolean
)