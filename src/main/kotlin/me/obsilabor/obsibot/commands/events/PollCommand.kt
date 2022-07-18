package me.obsilabor.obsibot.commands.events

import com.kotlindiscord.kord.extensions.checks.anyGuild
import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.application.slash.ephemeralSubCommand
import com.kotlindiscord.kord.extensions.commands.application.slash.publicSubCommand
import com.kotlindiscord.kord.extensions.commands.converters.impl.long
import com.kotlindiscord.kord.extensions.commands.converters.impl.string
import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import dev.kord.common.Color
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.channel.createMessage
import dev.kord.rest.builder.message.create.actionRow
import dev.kord.rest.builder.message.create.embed
import me.obsilabor.obsibot.commands.CommandExtension
import me.obsilabor.obsibot.data.Poll
import me.obsilabor.obsibot.localization.globalText
import me.obsilabor.obsibot.localization.localText
import me.obsilabor.obsibot.utils.*

@KordPreview
class PollCommand : CommandExtension("poll", "command.poll.description") {

    override suspend fun setup() {
        ephemeralSlashCommand {
            name = "poll"
            description = globalText("command.poll.description")

            check { anyGuild() }

            ephemeralSubCommand(::PollCreateArgs) {
                name = "create"
                description = globalText("command.poll.create.description")

                action {
                    val obsiGuild = guild?.asGuild()?.obsify() ?: guild?.asGuild()?.createObsiGuild()!!
                    if(member?.asMember()?.hasRole(obsiGuild.pollRole?: Snowflake(0)) == false) {
                        respond {
                            embed {
                                title = localText("generic.nopermissions.short", obsiGuild)
                                description = localText("command.poll.rolerequired", obsiGuild)
                                applyDefaultFooter()
                            }
                        }
                        return@action
                    }
                    val customId = StringUtils.getRandomID()+user.id.toString()
                    val options = arguments.options.split(",")
                    val map = hashMapOf<String, Int>()
                    options.forEach {
                        map[it] = 0
                    }
                    var totalVotes = 0
                    map.values.forEach {
                        totalVotes+=it
                    }
                    if(totalVotes == 0) {
                        totalVotes = 1
                    }
                    val message = channel.createMessage {
                        content = "**${localText("poll", obsiGuild)}: ${arguments.what}**"
                        embed {
                            color = Color(7462764)
                            author {
                                name = member?.asMember()?.displayName
                                icon = user.asUser().avatar?.url
                            }
                            title = localText("poll", obsiGuild) + ": ${arguments.what}"
                            val builder = StringBuilder()
                            options.forEachIndexed { index, it ->
                                kotlin.runCatching {
                                    val votes = map.getOrDefault(it, 0)
                                    val percentage = votes / totalVotes
                                    builder.append("${index+1}: $it - $percentage% - $votes ${localText("poll.votes", obsiGuild)}")
                                    builder.appendLine()
                                }.onFailure {
                                    it.printStackTrace()
                                }
                            }
                            builder.appendLine()
                            builder.appendLine(localText("poll.instructions", hashMapOf("endtimestamp" to arguments.endTimestamp/1000), obsiGuild))
                            description = builder.toString()
                            applyDefaultFooter()
                        }
                        actionRow {
                            selectMenu(customId) {
                                options.forEach {
                                    option(it, it) {
                                        description = localText("poll.option.description", hashMapOf("option" to it), obsiGuild)
                                    }
                                }
                            }
                        }
                    }

                    val poll = Poll(guild?.id?:return@action, channel.id, customId, message.id, user.id, map, arguments.endTimestamp, hashMapOf(), false)
                    obsiGuild.adoptNewPoll(poll)
                    obsiGuild.update()
                    respond {
                        content = localText("command.poll.create.success", obsiGuild)
                    }
                }
            }
            publicSubCommand(::PollEndArgs) {
                name = "end"
                description = globalText("command.poll.end.description")

                action {
                    val guild = this.getGuild()?.asGuild() ?: return@action
                    val obsiGuild = guild.obsify() ?: return@action
                    val poll = obsiGuild.polls?.firstOrNull { it.messageId.value.toLong() == arguments.id } ?: return@action
                    obsiGuild.refreshPollVotes(
                        Poll(
                            poll.guildId,
                            poll.channelId,
                            poll.interactionId,
                            poll.messageId,
                            poll.owner,
                            poll.options,
                            System.currentTimeMillis()-1,
                            poll.voters,
                            false
                        )
                    )
                    obsiGuild.update()
                    respond {
                        content = "ok :ok_hand:"
                    }
                }
            }
        }
    }

    inner class PollCreateArgs : Arguments() {
        val what by string {
            name = "what"
            description = globalText("command.poll.create.argument.what.description")
        }

        val endTimestamp by long {
            name = "endtimestamp"
            description = globalText("command.poll.create.argument.endtimestamp.description")
        }

        val options by string {
            name = "options"
            description = globalText("command.poll.create.argument.options.description")
        }
    }

    inner class PollEndArgs : Arguments() {
        val id by long {
            name = "id"
            description = globalText("command.poll.end.argument.id.description")
        }
    }
}