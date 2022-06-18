package me.obsilabor.obsibot.minecraft.modding

@kotlinx.serialization.Serializable
abstract class CommonMappingsVersion {
    abstract val gameVersion: String
    abstract val maven: String
}