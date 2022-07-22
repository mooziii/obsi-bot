package me.obsilabor.obsibot.commands.contextmenu

import com.kotlindiscord.kord.extensions.checks.anyGuild
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.modules.unsafe.annotations.UnsafeAPI
import com.kotlindiscord.kord.extensions.modules.unsafe.extensions.unsafeMessageCommand
import com.kotlindiscord.kord.extensions.modules.unsafe.types.InitialMessageCommandResponse
import dev.kord.common.annotation.KordPreview
import dev.kord.core.entity.interaction.GuildMessageCommandInteraction
import me.obsilabor.obsibot.ObsiBot
import me.obsilabor.obsibot.check.hasRole
import me.obsilabor.obsibot.check.obsiGuild
import me.obsilabor.obsibot.modals.TagCreationModal

@KordPreview
class TagMessageCommand : Extension() {
    override val name = "tag-create"

    @UnsafeAPI
    override suspend fun setup() {
        unsafeMessageCommand {
            initialResponse = InitialMessageCommandResponse.None
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