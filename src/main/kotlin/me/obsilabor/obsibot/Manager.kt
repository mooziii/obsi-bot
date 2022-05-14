package me.obsilabor.obsibot

import dev.kord.common.annotation.KordPreview
import joptsimple.OptionParser

@KordPreview
suspend fun main(args: Array<String>) {
    val parser = object : OptionParser() {
        init {
            accepts("language", "Sets the global language of this instance")
                .withRequiredArg()
                .ofType(String::class.java)
                .describedAs("The language file name")
            acceptsAll(listOf("?", "help", "h"), "Prints the help")
            accepts("setup", "Prints help on how to setup the bot")
        }
    }
    val options = parser.parse(*args)
    if(options.has("?")) {
        parser.printHelpOn(System.out)
        return
    }
    ObsiBot.main(options)
}