package me.obsilabor.obsibot.commands.information

import com.kotlindiscord.kord.extensions.DISCORD_FUCHSIA
import com.kotlindiscord.kord.extensions.checks.anyGuild
import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.application.slash.publicSubCommand
import com.kotlindiscord.kord.extensions.commands.converters.impl.string
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import com.kotlindiscord.kord.extensions.modules.unsafe.annotations.UnsafeAPI
import com.kotlindiscord.kord.extensions.modules.unsafe.extensions.unsafeSubCommand
import com.kotlindiscord.kord.extensions.modules.unsafe.types.InitialSlashCommandResponse
import com.kotlindiscord.kord.extensions.types.editingPaginator
import com.kotlindiscord.kord.extensions.types.respond
import dev.kord.common.annotation.KordPreview
import dev.kord.core.entity.interaction.GuildApplicationCommandInteraction
import me.obsilabor.obsibot.check.hasRole
import me.obsilabor.obsibot.check.obsiGuild
import me.obsilabor.obsibot.commands.CommandExtension
import me.obsilabor.obsibot.localization.globalText
import me.obsilabor.obsibot.modals.TagCreationModal
import me.obsilabor.obsibot.utils.applyDefaultFooter
import me.obsilabor.obsibot.utils.obsiGuild

@OptIn(UnsafeAPI::class)
@KordPreview
class TagCommand : CommandExtension("tag") {
    private val entriesPerPage = 15

    override suspend fun setup() {
        publicSlashCommand {
            name = "tag"
            description = globalText(descriptionKey)

            check { anyGuild() }

            publicSubCommand {
                name = "list"
                description = globalText("command.tag.list.description")

                action {
                    val tagList = obsiGuild().tags.keys.toTypedArray()
                    if (tagList.isEmpty()) {
                        respond {
                            content = "Empty list"
                        }
                        return@action
                    }
                    editingPaginator("tags") {
                        owner = member
                        val pagesNeeded = if(tagList.size % entriesPerPage == 0) tagList.size / entriesPerPage else tagList.size / entriesPerPage + 1
                        var i = 0
                        repeat(pagesNeeded) { _ ->
                            page("tags") {
                                color = DISCORD_FUCHSIA
                                title = "Tags"
                                description = buildString {
                                    repeat(entriesPerPage) { _ ->
                                        runCatching {
                                            appendLine("`${i+1}.` ${tagList[i]}")
                                        }
                                        i++
                                    }
                                }
                                applyDefaultFooter()
                            }
                        }
                    }.send()
                }
            }

            unsafeSubCommand {
                initialResponse = InitialSlashCommandResponse.None
                name = "create"
                description = globalText("command.tag.create.description")

                check { hasRole(obsiGuild().tagManagementRole) }

                action {
                    TagCreationModal.create(event.interaction as GuildApplicationCommandInteraction)
                }
            }

            publicSubCommand(::TagPostArguments) {
                name = "post"
                description = globalText("command.tag.post.description")

                action {
                    respond {
                        content = obsiGuild().tags[arguments.tag.lowercase()]
                    }
                }
            }
        }
    }

    inner class TagPostArguments : Arguments() {
        val tag by string {
            name = "tag"
            description = globalText("command.tag.post.argument.tag.description")
        }
    }
}