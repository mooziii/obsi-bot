package me.obsilabor.obsibot.commands

import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.application.slash.converters.impl.stringChoice
import com.kotlindiscord.kord.extensions.commands.application.slash.ephemeralSubCommand
import com.kotlindiscord.kord.extensions.commands.application.slash.publicSubCommand
import com.kotlindiscord.kord.extensions.commands.converters.impl.boolean
import com.kotlindiscord.kord.extensions.commands.converters.impl.defaultingBoolean
import com.kotlindiscord.kord.extensions.commands.converters.impl.string
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import com.kotlindiscord.kord.extensions.types.respondPublic
import dev.kord.common.annotation.KordPreview
import dev.kord.rest.builder.message.create.embed
import me.obsilabor.obsibot.ObsiBot
import me.obsilabor.obsibot.localization.globalText
import me.obsilabor.obsibot.localization.localText
import me.obsilabor.obsibot.minecraft.modding.GameVersion
import me.obsilabor.obsibot.minecraft.modding.Mappings
import me.obsilabor.obsibot.minecraft.modding.Toolchain
import me.obsilabor.obsibot.minecraft.modding.VersionManager
import me.obsilabor.obsibot.utils.applyDefaultFooter
import me.obsilabor.obsibot.utils.createObsiGuild
import me.obsilabor.obsibot.utils.obsify

@KordPreview
class MinecraftCommand : CommandExtension("minecraft", "command.minecraft.description") {

    override suspend fun setup() {
        publicSlashCommand {
            name = "minecraft"
            description = globalText(descriptionKey)

            guild(ObsiBot.TEST_SERVER_ID)

            publicSubCommand(::MinecraftModdingArgs) {
                name = "modding"
                description = globalText("command.minecraft.modding.description")

                action {
                    val toolchain = Toolchain.valueOf(arguments.toolchain.uppercase())
                    val mappings = Mappings.valueOf(arguments.mappings.uppercase())
                    val loaderDependency = "${toolchain.loaderGroupId}:${VersionManager.getLoaderVersion(toolchain)}"
                    val apiDependency = "${toolchain.apiGroupId}:${VersionManager.getAPIVersion(toolchain, GameVersion(arguments.gameVersion, true))}"
                    val mappingsDependency = VersionManager.getMappingsVersion(mappings, GameVersion(arguments.gameVersion, true))
                    val gradlePlugins = buildString {
                        append("plugins {")
                        appendLine("    kotlin(\"jvm\") version \"1.7.0\"")
                        toolchain.gradlePlugins.forEach {
                            val id = it.split(":")[0]
                            val version = it.split(":")[1]
                            appendLine("    id(\"$id\") version \"$version\"")
                        }
                        mappings.gradlePlugins.forEach {
                            val id = it.split(":")[0]
                            val version = it.split(":")[1]
                            appendLine("    id(\"$id\") version \"$version\"")
                        }
                        appendLine("}")
                    }
                    val gradleDependencies = buildString {
                        append("dependencies {")
                        appendLine("    minecraft(com.mojang:minecraft:${arguments.gameVersion})")
                        if(mappings.isLayered) {
                            appendLine("    mappings(loom.layered {")
                            appendLine("        addLayer(quiltMappings.mappings($mappingsDependency))")
                            appendLine("        officialMojangMappings()")
                            appendLine("    })")
                        } else {
                            appendLine("    mappings($mappingsDependency)")
                        }
                        appendLine("    modImplementation(\"$loaderDependency\")")
                        appendLine("    modImplementation(\"$apiDependency\")")
                        if(arguments.kotlinLibraries) {
                            appendLine("    modImplementation(\"net.fabricmc:fabric-language-kotlin:${VersionManager.getKotlinLibraryVersion()}\")")
                        }
                        appendLine("}")
                    }
                    val gradleRepositories = buildString {
                        append("pluginManagement {")
                        appendLine("    repositories {")
                        appendLine("        gradlePluginPortal()")
                        for (repository in toolchain.repositories) {
                            appendLine("        maven(\"$repository\")")
                        }
                        for (repository in mappings.gradleRepositories) {
                            appendLine("        maven(\"$repository\")")
                        }
                        appendLine("    }")
                        appendLine("}")
                    }
                    respondPublic {
                        embed {
                            title = "Minecraft Modding"
                            field {
                                name = "build.gradle.kts - Plugins"
                                value = buildString {
                                    append("```kotlin")
                                    appendLine(gradlePlugins)
                                    appendLine("```")
                                }
                            }
                            field {
                                name = "build.gradle.kts - Dependencies"
                                value = buildString {
                                    append("```kotlin")
                                    appendLine(gradleDependencies)
                                    appendLine("```")
                                }
                            }
                            field {
                                name = "settings.gradle.kts"
                                value = buildString {
                                    append("```kotlin")
                                    appendLine(gradleRepositories)
                                    appendLine("```")
                                }
                            }
                            applyDefaultFooter()
                        }
                    }
                }

            }

            ephemeralSubCommand(::MinecraftVersionsArgs) {
                name = "versions"
                description = globalText("command.minecraft.versions.description")

                action {
                    val guild = guild?.asGuildOrNull() ?: return@action
                    val obsiGuild = guild.obsify() ?: guild.createObsiGuild()
                    respond {
                        embed {
                            title = localText("command.minecraft.versions.embed.title", obsiGuild)
                            description = buildString {
                                VersionManager.getGameVersions().forEach {
                                    if(!it.stable || arguments.showSnapshots) {
                                        appendLine("*${it.version}*")
                                    } else {
                                        appendLine(it.version)
                                    }
                                }
                            }
                            applyDefaultFooter()
                        }
                    }
                }
            }
        }
    }

    inner class MinecraftVersionsArgs : Arguments() {
        val showSnapshots by defaultingBoolean {
            name = "show-snapshots"
            description = globalText("command.minecraft.versions.argument.showSnapshots")
            defaultValue = false
        }
    }

    inner class MinecraftModdingArgs : Arguments() {
        val toolchain by stringChoice {
            name = "toolchain"
            description = globalText("command.minecraft.modding.argument.toolchain")
            Toolchain.values().forEach {
                choice(it.capitalizedName, it.capitalizedName)
            }
        }

        val gameVersion by string {
            name = "game-version"
            description = globalText("command.minecraft.modding.argument.gameVersion")
            validate {
                //if(VersionManager.getGameVersions().map { it.version.lowercase() }.contains(this.value.lowercase())) {
                    pass()
                //} else {
                //    fail()
                //}
            }
        }

        val mappings by stringChoice {
            name = "mappings"
            description = globalText("command.minecraft.modding.argument.mappings")
            Mappings.values().forEach {
                choice(it.capitalizedName, it.capitalizedName)
            }
        }

        val kotlinLibraries by defaultingBoolean {
            name = "kotlin-libraries"
            description = globalText("command.minecraft.modding.argument.kotlinLibraries")
            defaultValue = true
        }
    }
}