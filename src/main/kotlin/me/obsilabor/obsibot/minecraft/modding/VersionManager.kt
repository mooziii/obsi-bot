package me.obsilabor.obsibot.minecraft.modding

import com.modrinth.api.Version
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import me.obsilabor.obsibot.ObsiBot.ktorClient

object VersionManager {

    suspend fun getAPIVersion(toolchain: Toolchain, gameVersion: GameVersion): String {
        val request = ktorClient.request(toolchain.apiUrl) {
            method = HttpMethod.Get
            parameter("game_versions", "[\"${gameVersion.version}\"]")
        }
        val response = request.body<List<Version>>()
        return response.firstOrNull()?.versionNumber ?: "No version available"
    }

    suspend fun getKotlinLibraryVersion(): String {
        val request = ktorClient.request("https://api.modrinth.com/v2/project/Ha28R6CL/version") {
            method = HttpMethod.Get
        }
        val response = request.body<List<Version>>()
        return response.firstOrNull()?.versionNumber ?: "No version available"
    }

    suspend fun getLoaderVersion(toolchain: Toolchain): String {
        val request = ktorClient.request(toolchain.loaderUrl) {
            method = HttpMethod.Get
        }
        val response = when(toolchain) {
            Toolchain.FABRIC -> request.body<List<FabricLoaderVersion>>()
            Toolchain.QUILT -> request.body<List<QuiltLoaderVersion>>()
        }
        return response.first().maven
    }

    suspend fun getMappingsVersion(mappings: Mappings, version: GameVersion): String {
        val request = ktorClient.request(mappings.url ?: return "loom.officialMojangMappings()") {
             method = HttpMethod.Get
        }
        val response = when(mappings) {
            Mappings.LAYERED_MOJANG_AND_QUILT -> request.body<List<QuiltMappingsVersion>>()
            Mappings.QUILT -> request.body<List<QuiltMappingsVersion>>()
            Mappings.YARN -> request.body<List<YarnMappingsVersion>>()
            else -> return "loom.officialMojangMappings()"
        }
        val maven = response.firstOrNull { it.gameVersion == version.version }?.maven ?: return "loom.officialMojangMappings()"
        return "\"$maven\""
    }

    suspend fun getGameVersions(): List<GameVersion> {
        val request = ktorClient.request("https://meta.quiltmc.org/v3/versions/game") {
            method = HttpMethod.Get
        }
        return request.body()
    }
}