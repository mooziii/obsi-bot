package me.obsilabor.obsibot.listeners

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.event
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.createRole
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.event.guild.GuildCreateEvent
import me.obsilabor.obsibot.ObsiBot
import me.obsilabor.obsibot.utils.createObsiGuild
import me.obsilabor.obsibot.utils.obsify

@KordPreview
class ReadyListener : Extension() {

    override val name: String = "readylistener"

    override suspend fun setup() {
        event<ReadyEvent> {
            action {
                ObsiBot.client = kord
            }
        }
        event<GuildCreateEvent> {
            action {
                val obsiGuild = event.guild.obsify() ?: event.guild.createObsiGuild()
                if(obsiGuild.giveawayRole == null || event.guild.getRoleOrNull(obsiGuild.giveawayRole ?: Snowflake(0)) == null) {
                    obsiGuild.giveawayRole = event.guild.createRole {
                        name = "Giveaway Permissions"
                        reason = "Created by obsi-bot (please don't delete)"
                    }.id
                }
            }
        }
    }
}