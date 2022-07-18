package me.obsilabor.obsibot.commands

import com.kotlindiscord.kord.extensions.checks.anyGuild
import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.application.slash.ephemeralSubCommand
import com.kotlindiscord.kord.extensions.commands.converters.impl.*
import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import com.kotlindiscord.kord.extensions.utils.hasPermission
import dev.kord.common.Color
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Permission
import dev.kord.core.behavior.channel.MessageChannelBehavior
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.behavior.reply
import dev.kord.core.entity.ReactionEmoji
import dev.kord.rest.builder.message.create.embed
import me.obsilabor.obsibot.ObsiBot
import me.obsilabor.obsibot.data.Giveaway
import me.obsilabor.obsibot.data.ObsiGuild
import me.obsilabor.obsibot.database.MongoManager
import me.obsilabor.obsibot.localization.globalText
import me.obsilabor.obsibot.localization.localText
import me.obsilabor.obsibot.utils.applyDefaultFooter
import me.obsilabor.obsibot.utils.createObsiGuild
import me.obsilabor.obsibot.utils.hasRole
import me.obsilabor.obsibot.utils.obsify
import org.litote.kmongo.eq

@KordPreview
class GiveawayCommand : CommandExtension("giveaway", "command.giveaway.description") {

    override suspend fun setup() {
        ephemeralSlashCommand {
            name = "giveaway"
            description = globalText("command.giveaway.description")

            check { anyGuild() }

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
                        message.addReaction(ReactionEmoji.Unicode("\uD83C\uDF89"))
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
            ephemeralSubCommand(::GiveawayRollArgs) {
                name = "roll"
                description = globalText("command.giveaway.roll.description")

                action {
                    val obsiGuild = guild?.asGuildOrNull()?.obsify() ?: guild?.asGuildOrNull()?.createObsiGuild()
                    if(obsiGuild == null) {
                        respond {
                            content = "obsiGuild == null"
                        }
                        return@action
                    }
                    val giveaway = obsiGuild.giveaways.firstOrNull { it.messageId.toString() == arguments.id } ?: return@action
                    if((member?.asMember()?.hasRole(obsiGuild.giveawayRole) == true && giveaway.owner == member?.id) || member?.asMember()?.hasPermission(Permission.Administrator) == true) {
                        val newList = arrayListOf(
                            Giveaway(
                                giveaway.owner,
                                giveaway.participants,
                                giveaway.messageId,
                                giveaway.channelId,
                                giveaway.guildId,
                                0,
                                giveaway.prize,
                                giveaway.prizeCount,
                                true
                            )
                        )
                        for (gvwy in obsiGuild.giveaways) {
                            if (gvwy.messageId != giveaway.messageId) {
                                newList.add(gvwy)
                            }
                        }
                        MongoManager.guilds.replaceOne(
                            ObsiGuild::id eq obsiGuild.id, obsiGuild.adoptGiveaways(newList)
                        )
                        (ObsiBot.client.getGuild(obsiGuild.id)?.getChannel(giveaway.channelId) as MessageChannelBehavior).getMessage(giveaway.messageId)
                            .reply {
                                content = localText(
                                    "giveaway.winners",
                                    hashMapOf("winners" to giveaway.roll().joinToString(", ") { "<@${it.value}>" }),
                                    obsiGuild
                                )
                            }
                        respond {
                            content = localText("command.giveaway.roll.success", obsiGuild)
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

    inner class GiveawayRollArgs : Arguments() {
        val id by string {
            name = "id"
            description = globalText("command.giveaway.roll.argument.id.description")
        }
    }
}