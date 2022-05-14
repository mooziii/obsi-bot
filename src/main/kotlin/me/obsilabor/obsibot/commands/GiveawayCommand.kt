package me.obsilabor.obsibot.commands

import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.application.slash.ephemeralSubCommand
import com.kotlindiscord.kord.extensions.commands.application.slash.publicSubCommand
import com.kotlindiscord.kord.extensions.commands.converters.impl.*
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import com.kotlindiscord.kord.extensions.types.respondEphemeral
import com.kotlindiscord.kord.extensions.types.respondPublic
import com.kotlindiscord.kord.extensions.utils.hasRole
import dev.kord.common.annotation.KordPreview
import dev.kord.core.behavior.channel.createMessage
import dev.kord.rest.builder.message.create.actionRow
import dev.kord.rest.builder.message.create.embed
import me.obsilabor.obsibot.ObsiBot
import me.obsilabor.obsibot.localization.globalText
import me.obsilabor.obsibot.localization.localText
import me.obsilabor.obsibot.utils.applyDefaultFooter
import me.obsilabor.obsibot.utils.createObsiGuild
import me.obsilabor.obsibot.utils.hasRole
import me.obsilabor.obsibot.utils.obsify

@KordPreview
class GiveawayCommand : Extension() {
    override val name: String = "giveawaycommand"

    override suspend fun setup() {
        ephemeralSlashCommand {
            name = "giveaway"
            description = globalText("command.giveaway.description")

            guild(ObsiBot.TEST_SERVER_ID)

            ephemeralSubCommand(::GiveawayCreateArgs) {
                name = "create"
                description = globalText("command.giveaway.create.description")

                action {
                    val obsiGuild = guild?.asGuildOrNull()?.obsify() ?: guild?.asGuildOrNull()?.createObsiGuild()
                    if(obsiGuild == null) {
                        respond {
                            content = "obsiGuild == null"
                        }
                        return@action
                    }
                    if(member?.asMember()?.hasRole(obsiGuild.giveawayRole) == true) {
                        respond {
                            content = "hi"
                        }
                    } else {
                        respond {
                            embed {
                                title = localText("generic.nopermissions.short", obsiGuild)
                                description = localText("command.giveaway.rolerequired", obsiGuild)
                                applyDefaultFooter()
                            }
                        }
                    }
                }
            }
        }
    }


    inner class GiveawayCreateArgs : Arguments() {

        val prize by string {
            name = "prize"
            description = globalText("command.giveaway.create.argument.prize.description")
        }

        val prizeCount by int {
            name = "prizecount"
            description = globalText("command.giveaway.create.argument.prizecount.description")
        }

        val endTimestamp by long {
            name = "endtimestamp"
            description = globalText("command.giveaway.create.argument.endtimestamp.description")
        }

    }
}