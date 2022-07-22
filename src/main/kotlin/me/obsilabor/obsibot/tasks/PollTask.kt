package me.obsilabor.obsibot.tasks

import dev.kord.common.annotation.KordPreview
import dev.kord.core.behavior.channel.MessageChannelBehavior
import dev.kord.core.behavior.reply
import kotlinx.coroutines.launch
import me.obsilabor.obsibot.ObsiBot
import me.obsilabor.obsibot.data.Poll
import me.obsilabor.obsibot.database.MongoManager
import me.obsilabor.obsibot.localization.localText
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
                    polls.addAll(obsiGuild.polls.toList())
                }
                for (poll in polls) {
                    if (!poll.ended && poll.end != 0L && poll.options.isNotEmpty()) {
                        val guild = ObsiBot.client.getGuild(poll.guildId) ?: continue
                        val obsiGuild = guild.obsify() ?: continue
                        if (System.currentTimeMillis() >= poll.end) {
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
                            (guild.getChannel(poll.channelId) as MessageChannelBehavior).getMessage(poll.messageId).reply {
                                content = localText("poll.winner", hashMapOf("option" to poll.winner), obsiGuild)
                            }
                        }
                    }
                }
            }
        }.onFailure {
            it.printStackTrace()
        }
    }
}