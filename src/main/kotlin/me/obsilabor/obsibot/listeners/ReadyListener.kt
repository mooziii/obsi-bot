package me.obsilabor.obsibot.listeners

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.event
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.createRole
import dev.kord.core.event.gateway.ReadyEvent
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
                println("Bot started in ${System.currentTimeMillis()- ObsiBot.initTime}ms")
                ObsiBot.isFullyFunctional = true
                ObsiBot.client = kord
                // TODO: add api for this
                kord.guilds.toList().forEach {
                    val obsiGuild = it.obsify() ?: it.createObsiGuild()
                    if(obsiGuild.giveawayRole == null || it.getRoleOrNull(obsiGuild.giveawayRole ?: Snowflake(0)) == null) {
                        obsiGuild.adoptGiveawayRoleId(it.createRole {
                            name = "Giveaway Permissions"
                            reason = "Created by obsi-bot (please don't delete)"
                        }.id)
                        obsiGuild.update()
                    }
                    if(obsiGuild.pollRole == null || it.getRoleOrNull(obsiGuild.pollRole ?: Snowflake(0)) == null) {
                        obsiGuild.adoptPollRoleId(it.createRole {
                            name = "Poll Permissions"
                            reason = "Created by obsi-bot (please don't delete)"
                        }.id)
                        obsiGuild.update()
                    }
                    if(obsiGuild.blacklistManagementRole == null || it.getRoleOrNull(obsiGuild.blacklistManagementRole ?: Snowflake(0)) == null) {
                        obsiGuild.adoptBlacklistManagementRole(it.createRole {
                            name = "Blacklist Management Permissions"
                            reason = "Created by obsi-bot (please don't delete)"
                        }.id)
                        obsiGuild.update()
                    }
                    if(obsiGuild.blacklistBypassRole == null || it.getRoleOrNull(obsiGuild.blacklistBypassRole ?: Snowflake(0)) == null) {
                        obsiGuild.adoptBlacklistBypassRole(it.createRole {
                            name = "Blacklist Bypass Permissions"
                            reason = "Created by obsi-bot (please don't delete)"
                        }.id)
                        obsiGuild.update()
                    }
                    if(obsiGuild.tagManagementRole == null || it.getRoleOrNull(obsiGuild.tagManagementRole ?: Snowflake(0)) == null) {
                        obsiGuild.adoptTagManagementRole(it.createRole {
                            name = "Tag Management Permissions"
                            reason = "Created by obsi-bot (please don't delete)"
                        }.id)
                        obsiGuild.update()
                    }
                }
            }
        }
    }
}