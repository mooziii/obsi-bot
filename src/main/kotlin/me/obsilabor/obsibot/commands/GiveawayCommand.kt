package me.obsilabor.obsibot.commands

import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.application.slash.ephemeralSubCommand
import com.kotlindiscord.kord.extensions.commands.application.slash.publicSubCommand
import com.kotlindiscord.kord.extensions.commands.converters.impl.*
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import dev.kord.common.annotation.KordPreview
import dev.kord.core.behavior.channel.createMessage
import dev.kord.rest.builder.message.create.actionRow
import me.obsilabor.obsibot.ObsiBot
import me.obsilabor.obsibot.localization.globalText

@KordPreview
class GiveawayCommand : Extension() {
    override val name: String = "giveawaycommand"

    override suspend fun setup() {
        publicSlashCommand {
            name = "giveawayd"
            description = globalText("command.giveaway.description")

            guild(ObsiBot.TEST_SERVER_ID)



            publicSubCommand(::GiveawayCreateArgs) {
                name = "create"
                description = globalText("command.giveaway.description")

                action {
                    channel.createMessage {
                        content = "hi"
                        actionRow {
                            selectMenu("smiley") {
                                option("mache ein smiley", "smiley") {
                                    description = "smiley zu viel gemacht"
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    inner class GiveawayCreateArgs : Arguments() {

        val prize by string {
            name = "prize"
            description = globalText("command.giveaway.create.argument.prize.description")
        }

        val prizeCount by int {
            name = "prizecount"
            description = globalText("command.giveaway.create.argument.prizecount.description")
        }

        val endTimestamp by long {
            name = "endtimestamp"
            description = globalText("command.giveaway.create.argument.endtimestamp.description")
        }

    }
}