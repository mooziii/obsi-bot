package me.obsilabor.obsibot.commands

import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.application.slash.ephemeralSubCommand
import com.kotlindiscord.kord.extensions.commands.converters.impl.*
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import com.kotlindiscord.kord.extensions.utils.addReaction
import dev.kord.common.Color
import dev.kord.common.annotation.KordPreview
import dev.kord.core.behavior.channel.createMessage
import dev.kord.rest.builder.message.create.embed
import me.obsilabor.obsibot.ObsiBot
import me.obsilabor.obsibot.data.Giveaway
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
                        val message = channel.createMessage {
                            content = ":tada: **GIVEAWAY** :tada:"
                            embed {
                                color = Color(15676260)
                                author {
                                    name = localText("giveaway.embed.author", hashMapOf("owner" to member?.asMember()?.displayName!!), obsiGuild)
                                    icon = member?.asUserOrNull()?.avatar?.url
                                }
                                title = "${arguments.prizeCount}x ${arguments.prize}"
                                description = localText(
                                    "giveaway.embed.description",
                                    hashMapOf(
                                        "prize" to arguments.prize,
                                        "prizecount" to arguments.prizeCount,
                                        "emoji" to ":tada:",
                                        "end" to arguments.endTimestamp/1000,
                                        "owner" to member?.id?.value!!
                                    ),
                                    obsiGuild
                                )
                                applyDefaultFooter()
                            }
                        }
                        message.addReaction("U+1F389")
                        obsiGuild.adoptNewGiveaway(Giveaway(
                            member?.id ?: return@action,
                            arrayListOf(),
                            message.id,
                            message.channelId,
                            message.getGuild().id,
                            arguments.endTimestamp,
                            arguments.prize,
                            arguments.prizeCount,
                            false
                        ))
                        obsiGuild.update()
                        respond {
                            content = localText("command.giveaway.create.success", obsiGuild)
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