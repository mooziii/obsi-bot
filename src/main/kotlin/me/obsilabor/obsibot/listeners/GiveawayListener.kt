package me.obsilabor.obsibot.listeners

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.event
import dev.kord.common.annotation.KordPreview
import dev.kord.core.event.message.ReactionAddEvent
import me.obsilabor.obsibot.utils.obsify

@KordPreview
class GiveawayListener : Extension() {

    override val name: String = "giveawaylistener"

    override suspend fun setup() {
        event<ReactionAddEvent> {
            action {
                if(event.user.asUser().isBot) {
                    return@action
                }
                val obsiGuild = event.guild?.asGuildOrNull()?.obsify() ?: return@action
                val giveaway = obsiGuild.giveaways.firstOrNull { it.messageId == event.messageId } ?: return@action
                if(giveaway.rolled) {
                    return@action
                }
                if(giveaway.participants.contains(event.userId)) {
                    return@action //why should someone leave the giveaway? will add this if needed
                }
                val newList = arrayListOf(
                    giveaway.addNewUser(event.userId)
                )
                obsiGuild.giveaways.forEach {
                    if(it.messageId != giveaway.messageId) {
                        newList.add(it)
                    }
                }
                obsiGuild.adoptGiveaways(newList)
                obsiGuild.update()
            }
        }
    }
}