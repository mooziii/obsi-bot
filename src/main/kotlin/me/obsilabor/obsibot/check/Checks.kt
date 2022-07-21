package me.obsilabor.obsibot.check

import com.kotlindiscord.kord.extensions.checks.types.CheckContext
import com.kotlindiscord.kord.extensions.utils.hasPermission
import com.kotlindiscord.kord.extensions.utils.hasPermissions
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Permission
import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.Role
import dev.kord.core.event.interaction.InteractionCreateEvent
import me.obsilabor.obsibot.ObsiBot
import me.obsilabor.obsibot.data.ObsiGuild
import me.obsilabor.obsibot.localization.localText
import me.obsilabor.obsibot.utils.createObsiGuild
import me.obsilabor.obsibot.utils.hasRole
import me.obsilabor.obsibot.utils.obsify

@KordPreview
suspend fun CheckContext<InteractionCreateEvent>.hasRole(role: Snowflake) {
    if (!passed) {
        return
    }
    val guildId = event.interaction.data.guildId.value ?: error("`hasRole` must always be combinded with `anyGuild`")
    val guild = ObsiBot.client.getGuild(guildId)?.asGuild()
    val roleName = guild?.getRole(role)?.asRole()?.name ?: "Invalid role id"
    if (event.interaction.user.asMember(guildId).hasRole(role)) {
        pass()
    } else {
        val obsiGuild = guild?.obsify() ?: guild?.createObsiGuild() ?: error("createObsiGuild failed")
        fail(localText("generic.nopermissions.rolerequired", hashMapOf("roleName" to roleName), obsiGuild))
    }
}

@KordPreview
suspend fun CheckContext<InteractionCreateEvent>.hasPermission(permission: Permission) {
    if (!passed) {
        return
    }
    val guildId = event.interaction.data.guildId.value ?: error("`hasPermission` must always be combinded with `anyGuild`")
    val guild = ObsiBot.client.getGuild(guildId)?.asGuild()
    val member = event.interaction.user.asMember(guildId)
    if (member.hasPermission(permission) || member.hasPermissions(Permission.Administrator)) {
        pass()
    } else {
        val obsiGuild = guild?.obsify() ?: guild?.createObsiGuild() ?: error("createObsiGuild failed")
        fail(localText("generic.nopermissions.long", hashMapOf("required" to permission::class.simpleName!!), obsiGuild))
    }
}

@KordPreview
suspend fun CheckContext<InteractionCreateEvent>.hasRole(role: Role) {
    hasRole(role.id)
}

@KordPreview
suspend fun CheckContext<InteractionCreateEvent>.obsiGuild() : ObsiGuild {
    val guildId = event.interaction.data.guildId.value ?: error("`obsiGuild` must always be combinded with `anyGuild`")
    val guild = ObsiBot.client.getGuild(guildId)?.asGuild()
    return guild?.obsify() ?: guild?.createObsiGuild() ?: error("createObsiGuild failed")
}