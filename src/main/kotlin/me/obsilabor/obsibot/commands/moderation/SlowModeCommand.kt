package me.obsilabor.obsibot.commands.moderation

import com.kotlindiscord.kord.extensions.checks.anyGuild
import com.kotlindiscord.kord.extensions.checks.hasPermission
import com.kotlindiscord.kord.extensions.checks.isNotInThread
import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.converters.impl.duration
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import dev.kord.common.entity.Permission
import dev.kord.core.behavior.channel.edit
import dev.kord.core.entity.channel.TextChannel
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.TimeZone
import me.obsilabor.obsibot.ObsiBot
import me.obsilabor.obsibot.commands.CommandExtension
import me.obsilabor.obsibot.localization.globalText
import me.obsilabor.obsibot.utils.compareTo
import me.obsilabor.obsibot.utils.toTotalSeconds

class SlowModeCommand() : CommandExtension("slowmode") {
    private val maxSlowModeDuration = DateTimePeriod(hours = 6)

    override suspend fun setup() {
        publicSlashCommand(::SlowModeArguments) {
            guild(ObsiBot.TEST_SERVER_ID)
            name = "slowmode"
            description = globalText(descriptionKey)

            check { anyGuild() }
            check { isNotInThread() }
            check { hasPermission(Permission.ManageChannels) }

            action {
                val channel = (channel.asChannel() as TextChannel)
                channel.edit {
                    rateLimitPerUser = arguments.duration.toTotalSeconds()
                }
            }
        }
    }

    inner class SlowModeArguments : Arguments() {
        val duration by duration {
            name = "duration"
            description = globalText("command.slowmode.argument")

            validate() {
                failIf(
                    "Slowmode cannot be longer than ${maxSlowModeDuration.hours} hours"
                ) { value > maxSlowModeDuration }
            }
        }
    }
}