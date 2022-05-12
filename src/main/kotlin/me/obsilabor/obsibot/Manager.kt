package me.obsilabor.obsibot

import joptsimple.OptionParser

suspend fun main(args: Array<String>) {
    val parser = object : OptionParser() {
        init {
            accepts("language", "Sets the global language of this instance")
                .withRequiredArg()
                .ofType(String::class.java)
                .describedAs("The language file name.")
        }
    }
    ObsiBot.main(parser.parse(*args))
}