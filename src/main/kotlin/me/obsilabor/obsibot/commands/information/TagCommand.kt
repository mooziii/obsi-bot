package me.obsilabor.obsibot.commands.information

import com.kotlindiscord.kord.extensions.checks.anyGuild
import com.kotlindiscord.kord.extensions.commands.application.slash.publicSubCommand
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import com.kotlindiscord.kord.extensions.types.editingPaginator
import dev.kord.common.annotation.KordPreview
import me.obsilabor.obsibot.ObsiBot
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
                    editingPaginator {
                        owner = member
                        val pagesNeeded = tagList.size/entriesPerPage
                        var i = 0
                        repeat(pagesNeeded) { _ ->
                            page {
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
        }
    }
}