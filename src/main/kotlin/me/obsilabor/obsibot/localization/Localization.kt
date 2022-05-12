package me.obsilabor.obsibot.localization

import kotlinx.serialization.decodeFromString
import me.obsilabor.obsibot.ObsiBot
import me.obsilabor.obsibot.data.ObsiGuild
import me.obsilabor.obsibot.utils.FileDownloader.downloadFile
import me.obsilabor.obsibot.utils.getOrCreateDirectory
import me.obsilabor.obsibot.utils.getOrCreateFile
import java.io.File

object Localization {

    const val DEFAULT_LANGUAGE = "en_us"
    var globalLanguage = DEFAULT_LANGUAGE
    val languages = hashMapOf<String, HashMap<String, String>>()
    private val langFolder = getOrCreateDirectory(File("lang"))

    suspend fun extractLanguageFiles() {
        downloadFile("https://raw.githubusercontent.com/Obsilabor/obsi-bot/main/lang/english.json", getOrCreateFile(File(langFolder, "english.json")))
        downloadFile("https://raw.githubusercontent.com/Obsilabor/obsi-bot/main/lang/german.json", getOrCreateFile(File(langFolder, "german.json")))
    }

    fun loadAllLanguageFiles() {
        langFolder.listFiles()?.forEach {
            if(it.name.endsWith(".json") && it.isFile) {
                runCatching {
                    languages[it.nameWithoutExtension] = ObsiBot.json.decodeFromString(it.readText())
                }.onFailure {_ ->
                    println("Could not load language \"${it.nameWithoutExtension}\"")
                }.onSuccess { _ ->
                    println("Loaded language \"${it.nameWithoutExtension}\" into memory.")
                }
            }
        }
    }
}

fun localText(key: String, map: HashMap<String, Any>, guild: ObsiGuild): String {
    val language = Localization.languages[guild.language] ?: Localization.languages[Localization.DEFAULT_LANGUAGE] ?: return "Error in localization loading"
    var string = language[key]
    map.keys.forEach {
        string = string?.replace(it, map[it].toString())
    }
    return string ?: globalText(key, map)
}

fun localText(key: String, guild: ObsiGuild): String {
    return localText(key, hashMapOf(), guild)
}

fun globalText(key: String, map: HashMap<String, Any>): String {
    val language = Localization.languages[Localization.globalLanguage] ?: return "Error in localization loading"
    var string = language[key]
    map.keys.forEach {
        string = string?.replace(it, map[it].toString())
    }
    return string ?: defaultLanguage(key, map)
}

private fun defaultLanguage(key: String, map: HashMap<String, Any>): String {
    val language = Localization.languages[Localization.DEFAULT_LANGUAGE] ?: return "Error in localization loading"
    var string = language[key]
    map.keys.forEach {
        string = string?.replace(it, map[it].toString())
    }
    return string ?: "Error in localization file."
}

fun globalText(key: String): String {
    return globalText(key, hashMapOf())
}