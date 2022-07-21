package me.obsilabor.obsibot.commands.moderation

import me.obsilabor.obsibot.check.obsiGuild
import com.kotlindiscord.kord.extensions.checks.anyGuild
import com.kotlindiscord.kord.extensions.checks.hasRole
import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.application.slash.ephemeralSubCommand
import com.kotlindiscord.kord.extensions.commands.converters.impl.defaultingBoolean
import com.kotlindiscord.kord.extensions.commands.converters.impl.defaultingString
import com.kotlindiscord.kord.extensions.commands.converters.impl.string
import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import dev.kord.common.Color
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Permission
import dev.kord.common.entity.Permissions
import dev.kord.rest.builder.message.create.embed
import me.obsilabor.obsibot.check.hasPermission
import me.obsilabor.obsibot.check.hasRole
import me.obsilabor.obsibot.check.hasRoleOrPermission
import me.obsilabor.obsibot.commands.CommandExtension
import me.obsilabor.obsibot.data.BlacklistedWord
import me.obsilabor.obsibot.localization.globalText
import me.obsilabor.obsibot.localization.localText
import me.obsilabor.obsibot.utils.applyDefaultFooter
import me.obsilabor.obsibot.utils.obsiGuild

@KordPreview
class BlacklistCommand : CommandExtension("blacklist") {
    override suspend fun setup() {
        ephemeralSlashCommand {
            name = "blacklist"
            description = globalText(descriptionKey)

            ephemeralSubCommand {

                check { anyGuild() }
                check { hasRoleOrPermission(obsiGuild().blacklistManagementRole, Permission.Administrator) }

                name = "view"
                description = globalText("command.blacklist.view.description")

                action {
                    val obsiGuild = obsiGuild()
                    respond {
                        embed {
                            color = Color(9146341)
                            title = "Blacklisted Words"
                            for (blacklistedWord in obsiGuild.blacklist) {
                                field {
                                    name = blacklistedWord.word
                                    value = """
                                        **${localText("why", obsiGuild)}:** ${blacklistedWord.why}
                                        **${
                                        localText(
                                            "filterVariants",
                                            obsiGuild
                                        )
                                    }:** ${
                                        if (blacklistedWord.checkVariants) localText(
                                            "yes",
                                            obsiGuild
                                        ) else localText("no", obsiGuild)
                                    }
                                    """.trimIndent()
                                }
                            }
                            applyDefaultFooter()
                        }
                    }
                }
            }

            ephemeralSubCommand(::BlacklistAddArgs) {

                check { anyGuild() }
                check { hasRoleOrPermission(obsiGuild().blacklistManagementRole, Permission.Administrator) }

                name = "add"
                description = globalText("command.blacklist.add.description")

                action {
                    val obsiGuild = obsiGuild()
                    obsiGuild.blacklist.add(BlacklistedWord(arguments.word, arguments.why, arguments.filterVariants))
                    obsiGuild.update()
                    respond {
                        embed {
                            color = Color(9146341)
                            title = localText("command.blacklist.updated", obsiGuild)
                            description = localText("command.blacklist.add.success", hashMapOf("word" to arguments.word), obsiGuild)
                            applyDefaultFooter()
                        }
                    }
                }
            }

            ephemeralSubCommand(::BlacklistRemoveArgs) {

                check { anyGuild() }
                check { hasRoleOrPermission(obsiGuild().blacklistManagementRole, Permission.Administrator) }

                name = "remove"
                description = globalText("command.blacklist.remove.description")

                action {
                    val obsiGuild = obsiGuild()
                    obsiGuild.blacklist.remove(obsiGuild.blacklist.first { it.why.equals(arguments.word, true) })
                    obsiGuild.update()
                    respond {
                        embed {
                            color = Color(9146341)
                            title = localText("command.blacklist.updated", obsiGuild)
                            description = localText("command.blacklist.remove.success", hashMapOf("word" to arguments.word), obsiGuild)
                            applyDefaultFooter()
                        }
                    }
                }
            }
        }
    }

    inner class BlacklistAddArgs : Arguments() {
        val word by string {
            name = "word"
            description = globalText("command.blacklist.argument.word.description")
        }
        val why by defaultingString {
            name = "why"
            description = globalText("command.blacklist.argument.why.description")
            defaultValue = "Swear words"
        }
        val filterVariants by defaultingBoolean {
            name = "filterVariants"
            description = globalText("command.blacklist.argument.filterVariants.description")
            defaultValue = true
        }
    }

    inner class BlacklistRemoveArgs : Arguments() {
        val word by string {
            name = "word"
            description = globalText("command.blacklist.argument.word.description")
        }
    }
}