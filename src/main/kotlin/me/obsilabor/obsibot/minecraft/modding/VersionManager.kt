package me.obsilabor.obsibot.minecraft.modding

import com.modrinth.api.Version
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.decodeFromString
import me.obsilabor.obsibot.ObsiBot
import me.obsilabor.obsibot.ObsiBot.ktorClient

object VersionManager {

    suspend fun getAPIVersion(toolchain: Toolchain, gameVersion: GameVersion): String {
        val response = ktorClient.get<HttpResponse>(toolchain.apiUrl) {
            parameter("game_versions", "[\"${gameVersion.version}\"]")
        }
        return ObsiBot.json.decodeFromString<List<Version>>(response.receive()).firstOrNull()?.versionNumber ?: "No version available"
    }

    suspend fun getKotlinLibraryVersion(): String {
        val response = ktorClient.get<HttpResponse>("https://api.modrinth.com/v2/project/Ha28R6CL/version")
        return ObsiBot.json.decodeFromString<List<Version>>(response.receive()).firstOrNull()?.versionNumber ?: "No version available"
    }

    suspend fun getLoaderVersion(toolchain: Toolchain): String {
        val response = when(toolchain) {
            Toolchain.FABRIC -> ObsiBot.json.decodeFromString<List<FabricLoaderVersion>>(ktorClient.get<HttpResponse>(toolchain.loaderUrl).receive())
            Toolchain.QUILT -> ObsiBot.json.decodeFromString<List<QuiltLoaderVersion>>(ktorClient.get<HttpResponse>(toolchain.loaderUrl).receive())
        }
        return response.first().maven
    }

    suspend fun getMappingsVersion(mappings: Mappings, version: GameVersion): String {
        val response = when(mappings) {
            Mappings.LAYERED_MOJANG_AND_QUILT -> ObsiBot.json.decodeFromString<List<QuiltLoaderVersion>>(ktorClient.get<HttpResponse>(mappings.url ?: return "loom.officialMojangMappings()").receive())
            Mappings.QUILT -> ObsiBot.json.decodeFromString<List<QuiltLoaderVersion>>(ktorClient.get<HttpResponse>(mappings.url ?: return "loom.officialMojangMappings()").receive())
            Mappings.YARN -> ObsiBot.json.decodeFromString<List<YarnMappingsVersion>>(ktorClient.get<HttpResponse>(mappings.url ?: return "loom.officialMojangMappings()").receive())
            else -> return "loom.officialMojangMappings()"
        }
        val maven = (response.firstOrNull { (it as CommonMappingsVersion).gameVersion == version.version } as CommonMappingsVersion).maven
        return "\"$maven\""
    }

    suspend fun getGameVersions(): List<GameVersion> {
        return ObsiBot.json.decodeFromString(ktorClient.get<HttpResponse>("https://meta.quiltmc.org/v3/versions/game").receive())
    }
}