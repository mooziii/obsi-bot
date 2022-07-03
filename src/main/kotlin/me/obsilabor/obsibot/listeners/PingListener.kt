package me.obsilabor.obsibot.listeners

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.event
import dev.kord.core.behavior.ban
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.event.message.MessageCreateEvent
import me.obsilabor.obsibot.localization.globalText

class PingListener : Extension() {

    override val name: String = "ping"

    override suspend fun setup() {
        event<MessageCreateEvent> {
            action {
                if (event.message.content == "<@${event.kord.selfId}>") {
                    event.message.channel.createMessage {
                        content = globalText("bot.ping")
                    }
                }
                var pingCount = 0
                event.message.content.toCharArray().forEachIndexed { index, it ->
                    if (it == '<') {
                        if (event.message.content.length >= index+2 && event.message.content[index + 1] == '@') {
                            pingCount++
                        }
                    }
                }
                if (pingCount >= 3) {
                    println("Trying to ban ${event.member?.tag}")
                    event.member?.ban {
                        reason = "Too many pings"
                        deleteMessagesDays = 7
                    }
                }
            }
        }
    }
}