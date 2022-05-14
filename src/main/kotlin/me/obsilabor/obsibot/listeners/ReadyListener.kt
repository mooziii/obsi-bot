package me.obsilabor.obsibot.listeners

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.event
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.createRole
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.event.guild.GuildCreateEvent
import kotlinx.coroutines.flow.toList
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
                kord.guilds.toList().forEach {
                    val obsiGuild = it.obsify() ?: it.createObsiGuild()
                    if(obsiGuild.giveawayRole == null || it.getRoleOrNull(obsiGuild.giveawayRole ?: Snowflake(0)) == null) {
                        obsiGuild.adoptGiveawayRoleId(it.createRole {
                            name = "Giveaway Permissions"
                            reason = "Created by obsi-bot (please don't delete)"
                        }.id)
                        obsiGuild.update()
                    }
                }
            }
        }
    }
}