package me.obsilabor.obsibot.commands

import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.application.slash.ephemeralSubCommand
import com.kotlindiscord.kord.extensions.commands.converters.impl.duration
import com.kotlindiscord.kord.extensions.commands.converters.impl.long
import com.kotlindiscord.kord.extensions.commands.converters.impl.string
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import dev.kord.common.annotation.KordPreview
import dev.kord.core.behavior.channel.createMessage
import dev.kord.rest.builder.message.create.actionRow
import dev.kord.rest.builder.message.create.embed
import me.obsilabor.obsibot.ObsiBot
import me.obsilabor.obsibot.data.Poll
import me.obsilabor.obsibot.localization.globalText
import me.obsilabor.obsibot.localization.localText
import me.obsilabor.obsibot.utils.StringUtils
import me.obsilabor.obsibot.utils.applyDefaultFooter
import me.obsilabor.obsibot.utils.createObsiGuild
import me.obsilabor.obsibot.utils.obsify

@KordPreview
class PollCommand : Extension() {

    override val name: String = "pollcommand"

    override suspend fun setup() {
        ephemeralSlashCommand {
            name = "poll"
            description = globalText("command.poll.description")

            guild(ObsiBot.TEST_SERVER_ID)

            ephemeralSubCommand(::PollCreateArgs) {
                name = "create"
                description = globalText("command.poll.create.description")

                action {
                    val obsiGuild = guild?.asGuild()?.obsify() ?: guild?.asGuild()?.createObsiGuild()!!
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
                    val message = channel.createMessage {
                        content = ":mega: **${localText("poll", obsiGuild)}** :mega:"
                        embed {
                            author {
                                name = member?.asMember()?.displayName
                                icon = user.asUser().avatar?.url
                            }
                            title = localText("poll", obsiGuild)
                            val builder = StringBuilder()
                            options.forEachIndexed { index, it ->
                                val votes = map.getOrDefault(it, 0)
                                val percentage = votes / totalVotes
                                builder.append("${index+1}: $it $percentage% - $votes ${localText("poll.votes", obsiGuild)}")
                                builder.appendLine()
                            }
                            builder.append(localText("poll.instructions", obsiGuild))
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

                    val poll = Poll(customId, message.id, user.id, map, arguments.endTimestamp)
                    obsiGuild.adoptNewPoll(poll)
                    obsiGuild.update()
                    respond {
                        content = localText("command.poll.create.success", obsiGuild)
                    }
                }
            }
        }
    }

    inner class PollCreateArgs : Arguments() {
        val endTimestamp by long {
            name = "endtimestamp"
            description = globalText("command.poll.create.arguments.endtimestamp.description")
        }

        val options by string {
            name = "options"
            description = globalText("command.poll.create.arguments.options.description")
        }
    }
}