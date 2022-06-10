package me.obsilabor.obsibot.commands

import com.kotlindiscord.kord.extensions.extensions.Extension

abstract class CommandExtension(val commandName: String, val descriptionKey: String) : Extension()