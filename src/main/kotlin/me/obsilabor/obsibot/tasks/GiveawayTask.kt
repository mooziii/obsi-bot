package me.obsilabor.obsibot.tasks

import dev.kord.common.annotation.KordPreview
import dev.kord.core.behavior.channel.MessageChannelBehavior
import dev.kord.core.behavior.reply
import kotlinx.coroutines.launch
import me.obsilabor.obsibot.ObsiBot
import me.obsilabor.obsibot.data.Giveaway
import me.obsilabor.obsibot.data.ObsiGuild
import me.obsilabor.obsibot.database.MongoManager
import me.obsilabor.obsibot.localization.localText
import me.obsilabor.obsibot.utils.obsify
import org.litote.kmongo.eq
import java.util.*

@KordPreview
class GiveawayTask : TimerTask() {

    override fun run() {
        if(!ObsiBot.isFullyFunctional) {
            return
        }
        kotlin.runCatching {
            ObsiBot.generalScope.launch {
                val giveaways = arrayListOf<Giveaway>()
                for (obsiGuild in MongoManager.guilds.find().toList()) {
                    giveaways.addAll(obsiGuild.giveaways)
                }
                for (giveaway in giveaways) {
                    if (!giveaway.rolled && giveaway.end != 0L) {
                        if (System.currentTimeMillis() >= giveaway.end) {
                            if (giveaway.participants.isEmpty()) {
                                continue
                            }
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
                            val obsiGuild = ObsiBot.client.getGuild(giveaway.guildId)?.obsify() ?: continue
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
                        }
                    }
                }
            }
        }.onFailure {
            it.printStackTrace()
        }
    }
}