package me.obsilabor.obsibot.minecraft.modding

import com.modrinth.api.Version
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import me.obsilabor.obsibot.ObsiBot.ktorClient

object VersionManager {

    suspend fun getAPIVersion(toolchain: Toolchain, gameVersion: GameVersion): String {
        val response = ktorClient.get<HttpResponse>(toolchain.apiUrl) {
            parameter("game_versions", "[\"${gameVersion.version}\"]")
        }
        return response.receive<List<Version>>().firstOrNull()?.versionNumber ?: "No version available"
    }

    suspend fun getKotlinLibraryVersion(): String {
        val response = ktorClient.get<HttpResponse>("https://api.modrinth.com/v2/project/Ha28R6CL/version")
        return response.receive<List<Version>>().firstOrNull()?.versionNumber ?: "No version available"
    }

    suspend fun getLoaderVersion(toolchain: Toolchain): String {
        val response = when(toolchain) {
            Toolchain.FABRIC -> ktorClient.get<HttpResponse>(toolchain.loaderUrl).receive<List<FabricLoaderVersion>>()
            Toolchain.QUILT -> ktorClient.get<HttpResponse>(toolchain.loaderUrl).receive<List<QuiltMappingsVersion>>()
        }
        return (response.first() as CommonLoaderVersion).maven
    }

    suspend fun getMappingsVersion(mappings: Mappings, version: GameVersion): String {
        val response = when(mappings) {
            Mappings.LAYERED_MOJANG_AND_QUILT -> ktorClient.get<HttpResponse>(mappings.url ?: return "loom.officialMojangMappings()").receive<List<QuiltMappingsVersion>>()
            Mappings.QUILT -> ktorClient.get<HttpResponse>(mappings.url ?: return "loom.officialMojangMappings()").receive<List<QuiltMappingsVersion>>()
            Mappings.YARN -> ktorClient.get<HttpResponse>(mappings.url ?: return "loom.officialMojangMappings()").receive<List<YarnMappingsVersion>>()
            else -> return "loom.officialMojangMappings()"
        }
        val maven = response.firstOrNull { it.gameVersion == version.version }?.maven ?: return "loom.officialMojangMappings()"
        return "\"$maven\""
    }

    suspend fun getGameVersions(): List<GameVersion> {
        return ktorClient.get<HttpResponse>("https://meta.quiltmc.org/v3/versions/game").receive()
    }
}