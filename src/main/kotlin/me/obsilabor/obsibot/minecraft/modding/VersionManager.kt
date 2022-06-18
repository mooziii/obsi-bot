package me.obsilabor.obsibot.minecraft.modding

import com.modrinth.api.Version
import io.ktor.client.request.*
import me.obsilabor.obsibot.ObsiBot.ktorClient

object VersionManager {

    suspend fun getAPIVersion(toolchain: Toolchain, gameVersion: GameVersion): String {
        val response = ktorClient.get<List<Version>>(toolchain.apiUrl) {
            parameter("game_versions", "[\"${gameVersion.version}\"]")
        }
        return response.firstOrNull()?.versionNumber ?: "No version available"
    }

    suspend fun getKotlinLibraryVersion(): String {
        val response = ktorClient.get<List<Version>>("https://api.modrinth.com/v2/project/Ha28R6CL/version")
        return response.firstOrNull()?.versionNumber ?: "No version available"
    }

    suspend fun getLoaderVersion(toolchain: Toolchain): String {
        val response = when(toolchain) {
            Toolchain.FABRIC -> ktorClient.get<List<FabricLoaderVersion>>(toolchain.loaderUrl)
            Toolchain.QUILT -> ktorClient.get<List<QuiltMappingsVersion>>(toolchain.loaderUrl)
        }
        return (response.first() as CommonLoaderVersion).maven
    }

    suspend fun getMappingsVersion(mappings: Mappings, version: GameVersion): String {
        val response = when(mappings) {
            Mappings.LAYERED_MOJANG_AND_QUILT -> ktorClient.get<List<QuiltMappingsVersion>>(mappings.url ?: return "loom.officialMojangMappings()")
            Mappings.QUILT -> ktorClient.get<List<QuiltMappingsVersion>>(mappings.url ?: return "loom.officialMojangMappings()")
            Mappings.YARN -> ktorClient.get<List<YarnMappingsVersion>>(mappings.url ?: return "loom.officialMojangMappings()")
            else -> return "loom.officialMojangMappings()"
        }
        val maven = response.firstOrNull { it.gameVersion == version.version }?.maven ?: return "loom.officialMojangMappings()"
        return "\"$maven\""
    }

    suspend fun getGameVersions(): List<GameVersion> {
        return ktorClient.get("https://meta.quiltmc.org/v3/versions/game")
    }
}