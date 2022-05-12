package me.obsilabor.obsibot

import com.kotlindiscord.kord.extensions.ExtensibleBot
import com.kotlindiscord.kord.extensions.utils.env
import dev.kord.common.entity.Snowflake
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import joptsimple.OptionSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import me.obsilabor.obsibot.listeners.PingListener
import me.obsilabor.obsibot.localization.Localization

object ObsiBot {

    val ktorClient by lazy {
        HttpClient(CIO) { expectSuccess = false }
    }

    val generalScope by lazy {
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    @OptIn(ExperimentalSerializationApi::class)
    val json by lazy {
        Json {
            prettyPrint = true
            encodeDefaults = true
            @Suppress("EXPERIMENTAL_API_USAGE")
            prettyPrintIndent = "  "
            ignoreUnknownKeys = true
        }
    }

    val TEST_SERVER_ID = Snowflake(env("TEST_SERVER"))

    private val TOKEN = env("TOKEN")

    suspend fun main(optionSet: OptionSet) {
        Localization.extractLanguageFiles()
        Localization.loadAllLanguageFiles()
        if(optionSet.hasArgument("language")) {
            Localization.globalLanguage = optionSet.valueOf("language").toString()
        }
        val bot = ExtensibleBot(TOKEN) {
            extensions {
                add(::PingListener)
            }
        }
        bot.start()
    }
}