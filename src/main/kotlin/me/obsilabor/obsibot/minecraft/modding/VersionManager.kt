package me.obsilabor.obsibot.minecraft.modding

import com.modrinth.api.Version
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.decodeFromString
import me.obsilabor.obsibot.ObsiBot
import me.obsilabor.obsibot.ObsiBot.ktorClient

object VersionManager {

    suspend fun getAPIVersion(toolchain: Toolchain, gameVersion: GameVersion): String {
        val response = ktorClient.get(toolchain.apiUrl) {
            parameter("game_versions", "[\"${gameVersion.version}\"]")
        }
            .body<HttpResponse>()
        return ObsiBot.json.decodeFromString<List<Version>>(response.body()).firstOrNull()?.versionNumber
            ?: "No version available"
    }

    suspend fun getKotlinLibraryVersion(): String {
        val response = ktorClient.get("https://api.modrinth.com/v2/project/Ha28R6CL/version").body<HttpResponse>()
        return ObsiBot.json.decodeFromString<List<Version>>(response.body()).firstOrNull()?.versionNumber
            ?: "No version available"
    }

    suspend fun getLoaderVersion(toolchain: Toolchain): String {
        val response = when (toolchain) {
            Toolchain.FABRIC -> ObsiBot.json.decodeFromString<List<FabricLoaderVersion>>(
                ktorClient.get(toolchain.loaderUrl).body<HttpResponse>().body()
            )
            Toolchain.QUILT -> ObsiBot.json.decodeFromString<List<QuiltLoaderVersion>>(
                ktorClient.get(toolchain.loaderUrl).body<HttpResponse>().body()
            )
        }
        return response.first().maven
    }

    suspend fun getMappingsVersion(mappings: Mappings, version: GameVersion): String {
        val response = when (mappings) {
            Mappings.LAYERED_MOJANG_AND_QUILT -> ObsiBot.json.decodeFromString<List<QuiltMappingsVersion>>(
                ktorClient.get(
                    mappings.url ?: return "loom.officialMojangMappings()"
                ).body<HttpResponse>().body()
            )
            Mappings.QUILT -> ObsiBot.json.decodeFromString<List<QuiltMappingsVersion>>(
                ktorClient.get(
                    mappings.url ?: return "loom.officialMojangMappings()"
                ).body<HttpResponse>().body()
            )
            Mappings.YARN -> ObsiBot.json.decodeFromString<List<YarnMappingsVersion>>(
                ktorClient.get(
                    mappings.url ?: return "loom.officialMojangMappings()"
                ).body<HttpResponse>().body()
            )
            else -> return "loom.officialMojangMappings()"
        }
        val maven = (response.firstOrNull { it.gameVersion == version.version } as CommonMappingsVersion).maven
        return "\"$maven:v2\""
    }

    suspend fun getGameVersions(): List<GameVersion> {
        return ObsiBot.json.decodeFromString(
            ktorClient.get("https://meta.quiltmc.org/v3/versions/game").body<HttpResponse>().body()
        )
    }
}