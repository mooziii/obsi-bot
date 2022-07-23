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
import dev.kord.rest.builder.message.create.embed
import me.obsilabor.obsibot.ObsiBot
import me.obsilabor.obsibot.check.hasRole
import me.obsilabor.obsibot.check.obsiGuild
import me.obsilabor.obsibot.commands.CommandExtension
import me.obsilabor.obsibot.localization.globalText
import me.obsilabor.obsibot.modals.TagCreationModal
import me.obsilabor.obsibot.utils.applyDefaultFooter
import me.obsilabor.obsibot.utils.obsiGuild

@UnsafeAPI
@KordPreview
class TagCommand : CommandExtension("tag") {
    private val entriesPerPage = 15

    override suspend fun setup() {
        publicSlashCommand {
            guild(ObsiBot.TEST_SERVER_ID)
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
                    }
                    respond {
                        embed {
                            color = DISCORD_FUCHSIA
                            title = "Tags"
                            description = buildString {
                                tagList.forEachIndexed { index, s ->
                                    appendLine("`${index+1}.` $s")
                                }
                            }
                            applyDefaultFooter()
                        }
                        /*
                        editingPaginator("tags") {
                            owner = member
                            val pagesNeeded = tagList.size/entriesPerPage
                            var i = 0
                            repeat(pagesNeeded) { _ ->
                                page("tags") {
                                    title = "Tags"
                                    description = buildString {
                                        repeat(entriesPerPage) { _ ->
                                            appendLine("`$i.` ${tagList[i]}")
                                            i++
                                        }
                                    }
                                }
                            }
                        }
                         */
                    }
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