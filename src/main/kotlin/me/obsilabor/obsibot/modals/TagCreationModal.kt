package me.obsilabor.obsibot.modals

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.event
import com.kotlindiscord.kord.extensions.utils.ackPublic
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.TextInputStyle
import dev.kord.core.behavior.interaction.modal
import dev.kord.core.behavior.interaction.response.createPublicFollowup
import dev.kord.core.entity.interaction.GuildApplicationCommandInteraction
import dev.kord.core.entity.interaction.GuildMessageCommandInteraction
import dev.kord.core.event.interaction.ModalSubmitInteractionCreateEvent
import me.obsilabor.obsibot.ObsiBot
import me.obsilabor.obsibot.localization.localText
import me.obsilabor.obsibot.utils.createObsiGuild
import me.obsilabor.obsibot.utils.obsify

@KordPreview
class TagCreationModal : Extension() {
    companion object {
        private const val MODAL_ID = "MODAL_CREATE_TAG"
        private const val TAG_ID_FIELD_ID = "TAG_SHORT"
        private const val TAG_CONTENT_FIELD_ID = "TAG_LONG"

        suspend fun create(interaction: GuildMessageCommandInteraction) {
            val obsiGuild = interaction.getGuild().obsify() ?: return
            interaction.modal(
                localText("modal.createTag.title", obsiGuild),
                MODAL_ID
            ) {
                actionRow {
                    textInput(
                        TextInputStyle.Paragraph,
                        TAG_ID_FIELD_ID,
                        localText("modal.createTag.short", obsiGuild)
                    ) {
                        allowedLength = 2..32
                        required = true
                        placeholder = localText("modal.createTag.short.placeholder", obsiGuild)
                    }
                    textInput(
                        TextInputStyle.Paragraph,
                        TAG_CONTENT_FIELD_ID,
                        localText("modal.createTag.long", obsiGuild)
                    ) {
                        value = interaction.target.asMessage().content
                    }
                }
            }
        }

        suspend fun create(interaction: GuildApplicationCommandInteraction) {
            val obsiGuild = interaction.getGuild().obsify() ?: return
            interaction.modal(
                localText("modal.createTag.title", obsiGuild),
                MODAL_ID
            ) {
                actionRow {
                    textInput(
                        TextInputStyle.Paragraph,
                        TAG_ID_FIELD_ID,
                        localText("modal.createTag.short", obsiGuild)
                    ) {
                        allowedLength = 2..32
                        required = true
                        placeholder = localText("modal.createTag.short.placeholder", obsiGuild)
                    }
                    textInput(
                        TextInputStyle.Paragraph,
                        TAG_CONTENT_FIELD_ID,
                        localText("modal.createTag.long", obsiGuild)
                    ) {
                        required = true
                        placeholder = localText("generic.placeholder", obsiGuild)
                    }
                }
            }
        }
    }

    override val name = "tag-create-modal-listener"

    override suspend fun setup() {
        event<ModalSubmitInteractionCreateEvent> {
            action {
                if(event.interaction.modalId != MODAL_ID) {
                    return@action
                }
                val guild = ObsiBot.client.getGuild(event.interaction.data.guildId.value ?: return@action) ?: return@action
                val obsiGuild = guild.obsify() ?: guild.createObsiGuild()
                val short = event.interaction.textInputs[TAG_ID_FIELD_ID]?.value ?: return@action
                val long = event.interaction.textInputs[TAG_CONTENT_FIELD_ID]?.value ?: return@action
                obsiGuild.tags[short] = long
                obsiGuild.update()
                event.interaction.ackPublic().createPublicFollowup {
                    content = localText("modal.createTag.success", hashMapOf("short" to short), obsiGuild)
                }
            }
        }
    }
}