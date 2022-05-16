package me.obsilabor.obsibot.tasks

import dev.kord.common.Color
import dev.kord.common.annotation.KordPreview
import dev.kord.core.behavior.channel.MessageChannelBehavior
import dev.kord.core.behavior.edit
import dev.kord.rest.builder.message.modify.actionRow
import dev.kord.rest.builder.message.modify.embed
import kotlinx.coroutines.launch
import me.obsilabor.obsibot.ObsiBot
import me.obsilabor.obsibot.data.Poll
import me.obsilabor.obsibot.database.MongoManager
import me.obsilabor.obsibot.localization.localText
import me.obsilabor.obsibot.utils.applyDefaultFooter
import me.obsilabor.obsibot.utils.obsify
import java.util.*

@KordPreview
class PollTask : TimerTask() {

    override fun run() {
        if(!ObsiBot.isFullyFunctional) {
            return
        }
        runCatching {
            ObsiBot.generalScope.launch {
                val polls = arrayListOf<Poll>()
                for (obsiGuild in MongoManager.guilds.find().toList()) {
                    polls.addAll(obsiGuild.polls?.toList() ?: return@launch)
                }
                for (poll in polls) {
                    if (!poll.ended && poll.end != 0L) {
                        if (System.currentTimeMillis() >= poll.end) {
                            val obsiGuild = ObsiBot.client.getGuild(poll.guildId)?.obsify() ?: continue
                            obsiGuild.refreshPollVotes(
                                Poll(
                                    poll.guildId,
                                    poll.channelId,
                                    poll.interactionId,
                                    poll.messageId,
                                    poll.owner,
                                    poll.options,
                                    0,
                                    poll.voters,
                                    true
                                )
                            )
                            obsiGuild.update()
                        }
                    }
                }
            }
        }.onFailure {
            it.printStackTrace()
        }
    }
}