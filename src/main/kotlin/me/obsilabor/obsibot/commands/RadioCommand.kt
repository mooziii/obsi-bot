package me.obsilabor.obsibot.commands

import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.application.slash.converters.impl.stringChoice
import com.kotlindiscord.kord.extensions.commands.application.slash.publicSubCommand
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import dev.kord.common.annotation.KordPreview
import me.obsilabor.obsibot.ObsiBot
import me.obsilabor.obsibot.config.ConfigManager
import me.obsilabor.obsibot.localization.globalText
import me.obsilabor.obsibot.utils.FileDownloader
import me.obsilabor.obsibot.utils.getOrCreateFile
import java.io.File

@KordPreview
class RadioCommand : Extension() {

    override val name: String
        get() = "radiocommand"

    override suspend fun setup() {
        println("Downloading official radiostream json file..")
        FileDownloader.downloadFile("https://raw.githubusercontent.com/mooziii/obsi-bot/main/config/radiostreams.json", getOrCreateFile(File("config", "radiostreams.json")))
        publicSlashCommand {
            name = "radio"
            description = globalText("command.radio.description")
            guild(ObsiBot.TEST_SERVER_ID)

            publicSubCommand {

            }

        }
    }

    inner class RadioArgs : Arguments() {

        val radioName by stringChoice {
            name = "radiostream"
            description = globalText("command.radio.play.argument.radiostream.description")
            ConfigManager.radioConfig.forEach {
                choice(it.name, it.language)
            }
        }

    }
}