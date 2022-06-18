package me.obsilabor.obsibot.minecraft.modding

@kotlinx.serialization.Serializable
data class FabricLoaderVersion(
    val separator: String,
    val build: Int,
    override val maven: String,
    val version: String,
    val stable: Boolean
) : CommonLoaderVersion()

