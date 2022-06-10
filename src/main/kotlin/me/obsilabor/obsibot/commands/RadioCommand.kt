package me.obsilabor.obsibot.commands

import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.application.slash.converters.impl.stringChoice
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.utils.hasPermission
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Permission
import dev.kord.core.entity.channel.VoiceChannel
import io.github.qbosst.kordex.builders.embed
import io.github.qbosst.kordex.commands.hybrid.publicHybridCommand
import io.github.qbosst.kordex.commands.hybrid.publicSubCommand
import me.obsilabor.obsibot.ObsiAudioBot
import me.obsilabor.obsibot.config.ConfigManager
import me.obsilabor.obsibot.config.RadioStreamConfig
import me.obsilabor.obsibot.localization.globalText
import me.obsilabor.obsibot.localization.localText
import me.obsilabor.obsibot.utils.*
import java.io.File

@KordPreview
class RadioCommand : Extension() {

    override val name: String
        get() = "radiocommand"

    override suspend fun setup() {
        println("Downloading official radiostream json file..")
        FileDownloader.downloadFile("https://raw.githubusercontent.com/mooziii/obsi-bot/main/config/radiostreams.json", getOrCreateFile(File("config", "radiostreams.json")))
        publicHybridCommand {
            name = "radio"
            description = globalText("command.radio.description")

            publicSubCommand(::RadioArgs) {
                name = "play"
                description = globalText("command.radio.play.description")
                action {
                    val guild = guild?.asGuildOrNull() ?: return@action
                    val member = member?.asMember() ?: return@action
                    val radioStream = findRadioStream(arguments.radioName) ?: return@action
                    respond {
                        content = ObsiAudioBot.playRadioStream(
                            radioStream.url,
                            guild,
                            member,
                            member.getVoiceState()?.getChannelOrNull() as VoiceChannel? ?: return@action,
                            radioStream.name,
                            radioStream
                        )
                    }
                }
            }
            publicSubCommand {
                name = "disconnect"
                description = globalText("command.radio.disconnect.description")

                action {
                    val guild = guild?.asGuildOrNull() ?: return@action
                    val obsiGuild = guild.obsify() ?: guild.createObsiGuild()
                    val member = member?.asMember() ?: return@action
                    if(member.hasPermission(Permission.ManageMessages) || member.hasPermission(Permission.Administrator)) {
                        ObsiAudioBot.disconnect(guild?.asGuild()!!)
                        respond {
                            content = ":ok_hand:"
                        }
                    } else {
                        respond {
                            embed {
                                title = localText("generic.nopermissions.short", obsiGuild)
                                description = localText("generic.nopermissions.short", obsiGuild)
                                applyDefaultFooter()
                            }
                        }
                    }
                }
            }
        }
    }

    inner class RadioArgs : Arguments() {
        val radioName by stringChoice {
            name = "radiostream"
            description = globalText("command.radio.play.argument.radiostream.description")
            ConfigManager.radioConfig.forEach {
                choice(it.name, it.name)
            }
        }
    }

    private fun findRadioStream(name: String): RadioStreamConfig? {
        for (radiostream in ConfigManager.radioConfig) {
            if(radiostream.name.lowercase() == name.lowercase()) {
                return radiostream
            }
        }
        return null
    }
}