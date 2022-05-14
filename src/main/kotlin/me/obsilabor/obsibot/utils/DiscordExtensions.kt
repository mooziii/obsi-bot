package me.obsilabor.obsibot.utils

import com.kotlindiscord.kord.extensions.utils.hasRole
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.Guild
import dev.kord.core.entity.Member
import dev.kord.rest.builder.message.EmbedBuilder
import me.obsilabor.obsibot.data.ObsiGuild
import me.obsilabor.obsibot.database.MongoManager
import org.litote.kmongo.eq
import org.litote.kmongo.findOne

@KordPreview
fun Guild.obsify(): ObsiGuild? {
    return MongoManager.guilds.findOne { ObsiGuild::id eq id }
}

@KordPreview
fun Guild.createObsiGuild(): ObsiGuild {
    val obsiGuild = ObsiGuild.newDocument(id)
    MongoManager.guilds.insertOne(obsiGuild)
    return obsiGuild
}

@KordPreview
suspend fun Member.hasRole(role: Snowflake): Boolean {
    return hasRole(guild.getRole(role))
}

@KordPreview
fun EmbedBuilder.applyDefaultFooter() {
    footer {
        text = "Made with ❤️ by mooz"
        icon = "https://avatars.githubusercontent.com/u/88035952?s=200&v=4"
    }
}