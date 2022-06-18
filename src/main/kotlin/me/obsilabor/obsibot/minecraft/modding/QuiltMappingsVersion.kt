package me.obsilabor.obsibot.minecraft.modding

@kotlinx.serialization.Serializable
data class QuiltMappingsVersion(
    override val gameVersion: String,
    val separator: String,
    val build: Int,
    override val maven: String,
    val version: String,
    val hashed: String
) : CommonMappingsVersion()