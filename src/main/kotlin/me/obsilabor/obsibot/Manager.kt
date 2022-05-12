package me.obsilabor.obsibot

import joptsimple.OptionParser

suspend fun main(args: Array<String>) {
    val parser = object : OptionParser() {
        init {}
    }
    ObsiBot.main(parser.parse(*args))
}