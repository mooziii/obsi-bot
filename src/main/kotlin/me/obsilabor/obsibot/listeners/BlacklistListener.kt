package me.obsilabor.obsibot.listeners

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.event
import dev.kord.common.annotation.KordPreview
import dev.kord.core.event.message.MessageCreateEvent
import me.obsilabor.obsibot.features.Blacklist
import me.obsilabor.obsibot.utils.hasRole
import me.obsilabor.obsibot.utils.obsify

@KordPreview
class BlacklistListener : Extension() {
    override val name = "blacklist-listener"

    override suspend fun setup() {
        event<MessageCreateEvent> {
            action {
                val content = event.message.content
                val obsiGuild = this.event.getGuild()?.obsify() ?: return@action
                if (event.member?.hasRole(obsiGuild.blacklistBypassRole ?: return@action) == true) {
                    return@action
                }
                obsiGuild.blacklist.forEach { entry ->
                    if(Blacklist.containsWord(content, entry.word, entry.checkVariants)) {
                        event.message.delete("Contains a blacklisted word")
                    }
                }
            }
        }
    }
}