package me.obsilabor.obsibot.commands

import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import com.kotlindiscord.kord.extensions.types.respondPublic
import dev.kord.common.annotation.KordPreview
import dev.kord.rest.builder.message.create.embed
import me.obsilabor.obsibot.ObsiBot
import me.obsilabor.obsibot.localization.localText
import me.obsilabor.obsibot.utils.applyDefaultFooter
import me.obsilabor.obsibot.utils.createObsiGuild
import me.obsilabor.obsibot.utils.obsify

@KordPreview
class HelpCommand : CommandExtension("help", "command.help.description") {
    override suspend fun setup() {
        publicSlashCommand {
            action {
                val obsiGuild = guild?.asGuild()?.obsify() ?: guild?.asGuild()?.createObsiGuild()!!
                respondPublic {
                    embed {
                        title = localText("help", obsiGuild)
                        description = localText("help.runwithargument", obsiGuild)
                        ObsiBot.commands.forEach {
                            field {
                                name = "/${it.name}"
                                value = localText(it.descriptionKey, obsiGuild)
                            }
                        }
                        applyDefaultFooter()
                    }
                }
            }
        }
    }
}