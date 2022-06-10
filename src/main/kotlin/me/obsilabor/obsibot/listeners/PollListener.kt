package me.obsilabor.obsibot.listeners

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.event
import dev.kord.common.Color
import dev.kord.common.annotation.KordPreview
import dev.kord.core.behavior.edit
import dev.kord.core.behavior.interaction.followUpEphemeral
import dev.kord.core.event.interaction.SelectMenuInteractionCreateEvent
import dev.kord.rest.builder.message.modify.actionRow
import dev.kord.rest.builder.message.modify.embed
import me.obsilabor.obsibot.localization.localText
import me.obsilabor.obsibot.utils.PercentageUtils
import me.obsilabor.obsibot.utils.applyDefaultFooter
import me.obsilabor.obsibot.utils.createObsiGuild
import me.obsilabor.obsibot.utils.obsify

@KordPreview
class PollListener : Extension() {
    override val name: String = "poll-listener"

    override suspend fun setup() {
        event<SelectMenuInteractionCreateEvent> {
            action {
                val interaction = event.interaction
                val guild = interaction.message?.getGuild() ?: return@action
                val obsiGuild = guild.obsify() ?: guild.createObsiGuild()
                val poll = obsiGuild.polls?.firstOrNull { it.interactionId == interaction.componentId } ?: return@action
                if(poll.ended || poll.end == 0L) {
                    interaction.acknowledgeEphemeral().followUpEphemeral {
                        content = localText("poll.ended", obsiGuild)
                    }
                    return@action
                }
                val previousOption = poll.voters[interaction.user.id]
                val selectedOption = interaction.values.first()
                if(previousOption != null) {
                    poll.options[previousOption] = (poll.options[previousOption] ?: 0)-1
                }
                poll.options[selectedOption] = (poll.options[selectedOption] ?: 0)+1
                poll.voters[interaction.user.id] = selectedOption
                obsiGuild.refreshPollVotes(poll)
                obsiGuild.update()
                // update message
                var totalVotes = 0
                poll.options.values.forEach {
                    totalVotes+=it
                }
                if(totalVotes == 0) {
                    totalVotes = 1
                }
                interaction.message?.edit {
                    content = "**${localText("poll", obsiGuild)}**"
                    embed {
                        color = Color(7462764)
                        author {
                            name = guild.getMember(poll.owner).asMember().displayName
                            icon = guild.getMember(poll.owner).asUser().avatar?.url
                        }
                        title = localText("poll", obsiGuild)
                        val builder = StringBuilder()
                        poll.options.keys.forEachIndexed { index, it ->
                            kotlin.runCatching {
                                val votes = poll.options.getOrDefault(it, 0)
                                val percentage = votes.toDouble() / totalVotes.toDouble()
                                builder.append("$it - ${PercentageUtils.toString(percentage)}% - $votes ${localText("poll.votes", obsiGuild)}")
                                builder.appendLine()
                            }.onFailure {
                                it.printStackTrace()
                            }
                        }
                        builder.appendLine()
                        builder.appendLine(localText("poll.instructions", hashMapOf("endtimestamp" to poll.end/1000), obsiGuild))
                        description = builder.toString()
                        applyDefaultFooter()
                    }
                    actionRow {
                        selectMenu(poll.interactionId) {
                            poll.options.forEach {
                                option(it.key, it.key) {
                                    description = localText("poll.option.description", hashMapOf("option" to it.key), obsiGuild)
                                }
                            }
                        }
                    }
                }
                interaction.acknowledgeEphemeral().followUpEphemeral {
                    content = localText("poll.voted", hashMapOf("option" to selectedOption), obsiGuild)
                }
            }
        }
    }
}