package me.obsilabor.obsibot.commands.moderation

import check.hasPermission
import com.kotlindiscord.kord.extensions.DISCORD_BLURPLE
import com.kotlindiscord.kord.extensions.checks.anyGuild
import com.kotlindiscord.kord.extensions.checks.hasPermission
import com.kotlindiscord.kord.extensions.checks.isNotInThread
import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.converters.impl.duration
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import com.kotlindiscord.kord.extensions.utils.toDuration
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Permission
import dev.kord.core.behavior.channel.edit
import dev.kord.core.entity.channel.TextChannel
import dev.kord.rest.builder.message.create.embed
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.TimeZone
import me.obsilabor.obsibot.ObsiBot
import me.obsilabor.obsibot.commands.CommandExtension
import me.obsilabor.obsibot.localization.globalText
import me.obsilabor.obsibot.localization.localText
import me.obsilabor.obsibot.utils.*

@KordPreview
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
                    rateLimitPerUser = arguments.duration.toDuration(TimeZone.UTC)
                }
                respond {
                    embed {
                        color = DISCORD_BLURPLE
                        title = localText("command.slowmode.success", hashMapOf("duration" to arguments.duration.string()), obsiGuild())
                        applyDefaultFooter()
                    }
                }
            }
        }
    }

    inner class SlowModeArguments : Arguments() {
        val duration by duration {
            name = "duration"
            description = globalText("command.slowmode.argument.duration.description")

            validate() {
                failIf(
                    "Slowmode cannot be longer than ${maxSlowModeDuration.hours} hours"
                ) { value > maxSlowModeDuration }
            }
        }
    }
}