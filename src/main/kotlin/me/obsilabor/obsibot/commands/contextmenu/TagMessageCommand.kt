package me.obsilabor.obsibot.commands.contextmenu

import com.kotlindiscord.kord.extensions.checks.anyGuild
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.ephemeralMessageCommand
import dev.kord.common.annotation.KordPreview
import dev.kord.core.entity.interaction.GuildMessageCommandInteraction
import me.obsilabor.obsibot.ObsiBot
import me.obsilabor.obsibot.check.hasRole
import me.obsilabor.obsibot.check.obsiGuild
import me.obsilabor.obsibot.modals.TagCreationModal

@KordPreview
class TagMessageCommand : Extension() {
    override val name = "tag-create"

    override suspend fun setup() {
        ephemeralMessageCommand {
            guild(ObsiBot.TEST_SERVER_ID)
            name = "Create Tag"
            check { anyGuild() }
            check { hasRole(obsiGuild().tagManagementRole) }
            action {
                TagCreationModal.create(event.interaction as GuildMessageCommandInteraction)
            }
        }
    }
}