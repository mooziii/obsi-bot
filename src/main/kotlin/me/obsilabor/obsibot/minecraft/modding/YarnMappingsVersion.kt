package me.obsilabor.obsibot.minecraft.modding

@kotlinx.serialization.Serializable
data class YarnMappingsVersion(
    override val gameVersion: String,
    val separator: String,
    val build: Int,
    override val maven: String,
    val version: String,
    val stable: Boolean
) : CommonMappingsVersion()