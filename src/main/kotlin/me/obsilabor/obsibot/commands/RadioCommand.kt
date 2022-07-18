package me.obsilabor.obsibot.commands

import com.kotlindiscord.kord.extensions.checks.anyGuild
import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.application.slash.converters.impl.stringChoice
import com.kotlindiscord.kord.extensions.commands.application.slash.publicSubCommand
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import com.kotlindiscord.kord.extensions.types.respondEphemeral
import com.kotlindiscord.kord.extensions.types.respondPublic
import com.kotlindiscord.kord.extensions.utils.hasPermission
import dev.kord.common.annotation.KordPreview
import dev.kord.common.annotation.KordVoice
import dev.kord.common.entity.Permission
import dev.kord.core.entity.channel.VoiceChannel
import dev.kord.rest.builder.message.create.embed
import me.obsilabor.obsibot.audio.ObsiAudioBot
import me.obsilabor.obsibot.config.ConfigManager
import me.obsilabor.obsibot.config.RadioStreamConfig
import me.obsilabor.obsibot.localization.globalText
import me.obsilabor.obsibot.localization.localText
import me.obsilabor.obsibot.utils.*
import java.io.File

@KordVoice
@KordPreview
class RadioCommand : CommandExtension("radio", "command.radio.description") {

    override suspend fun setup() {
        println("Downloading official radiostream json file..")
        FileDownloader.downloadFile("https://raw.githubusercontent.com/mooziii/obsi-bot/main/config/radiostreams.json", getOrCreateFile(File("config", "radiostreams.json")))
        publicSlashCommand {
            name = "radio"
            description = globalText("command.radio.description")

            check { anyGuild() }

            publicSubCommand(::RadioArgs) {
                name = "play"
                description = globalText("command.radio.play.description")
                action {
                    val guild = getGuild()?.asGuildOrNull() ?: return@action
                    val member = member?.asMember() ?: return@action
                    val radioStream = findRadioStream(arguments.radioName) ?: return@action
                    respond {
                        content = ObsiAudioBot.playRadioStream(
                            radioStream.url,
                            guild,
                            member,
                            member.getVoiceState().getChannelOrNull() as VoiceChannel,
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
                    val guild = getGuild()?.asGuildOrNull() ?: return@action
                    val obsiGuild = guild.obsify() ?: guild.createObsiGuild()
                    val member = member?.asMember() ?: return@action
                    if(member.hasPermission(Permission.ManageMessages) || member.hasPermission(Permission.Administrator)) {
                        ObsiAudioBot.disconnect(guild.asGuild())
                        respond {
                            content = ":ok_hand:"
                        }
                    } else {
                        respondEphemeral {
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