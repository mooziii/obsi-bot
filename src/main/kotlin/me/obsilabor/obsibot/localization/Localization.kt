package me.obsilabor.obsibot.localization

import me.obsilabor.obsibot.utils.FileDownloader
import me.obsilabor.obsibot.utils.getOrCreateDirectory
import me.obsilabor.obsibot.utils.getOrCreateFile
import java.io.File

object Localization {

    suspend fun extractLanguageFiles() {
        val langFolder = getOrCreateDirectory(File("lang"))
        FileDownloader.downloadFile("https://raw.githubusercontent.com/Obsilabor/obsi-bot/main/lang/en_us.json", getOrCreateFile(File(langFolder, "en_us.json")))
    }

}