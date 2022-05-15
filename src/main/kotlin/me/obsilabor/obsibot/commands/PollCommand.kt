package me.obsilabor.obsibot.commands

import com.kotlindiscord.kord.extensions.commands.application.slash.ephemeralSubCommand
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand
import me.obsilabor.obsibot.localization.globalText

class PollCommand : Extension() {

    override val name: String = "pollcommand"

    override suspend fun setup() {
        ephemeralSlashCommand {
            name = "poll"
            description = globalText("command.poll.description")

            ephemeralSubCommand {
                name = "create"
                description = globalText("command.poll.create.description")

                action {

                }
            }
        }
    }
}