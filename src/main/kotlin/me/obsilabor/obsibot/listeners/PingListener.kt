package me.obsilabor.obsibot.listeners

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.event
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.event.message.MessageCreateEvent
import me.obsilabor.obsibot.localization.globalText

class PingListener : Extension() {

    override val name: String = "ping"

    override suspend fun setup() {
        event<MessageCreateEvent> {
            action {
                if(event.message.content == "<@${event.kord.selfId}>") {
                    event.message.channel.createMessage {
                        content = globalText("bot.ping")
                    }
                }
            }
        }
    }
}