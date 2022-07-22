package me.obsilabor.obsibot.commands.information

import com.kotlindiscord.kord.extensions.checks.anyGuild
import com.kotlindiscord.kord.extensions.commands.application.slash.publicSubCommand
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import com.kotlindiscord.kord.extensions.types.editingPaginator
import com.kotlindiscord.kord.extensions.types.respond
import dev.kord.common.annotation.KordPreview
import me.obsilabor.obsibot.ObsiBot
import me.obsilabor.obsibot.check.hasRole
import me.obsilabor.obsibot.check.obsiGuild
import me.obsilabor.obsibot.commands.CommandExtension
import me.obsilabor.obsibot.localization.globalText
import me.obsilabor.obsibot.utils.obsiGuild

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
                    editingPaginator {
                        owner = member
                        val pagesNeeded = tagList.size/entriesPerPage
                        var i = 0
                        repeat(pagesNeeded) { _ ->
                            page("group") {
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
                }
            }

            publicSubCommand {
                name = "create"
                description = globalText("command.tag.create.description")

                check { hasRole(obsiGuild().tagManagementRole) }

                action {

                }
            }
        }
    }
}