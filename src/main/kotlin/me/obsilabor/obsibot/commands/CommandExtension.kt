package me.obsilabor.obsibot.commands

import com.kotlindiscord.kord.extensions.extensions.Extension

abstract class CommandExtension(override val name: String, val descriptionKey: String = "command.$name.description") : Extension()