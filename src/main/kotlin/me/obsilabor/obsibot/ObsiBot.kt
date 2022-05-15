package me.obsilabor.obsibot

import com.kotlindiscord.kord.extensions.ExtensibleBot
import com.kotlindiscord.kord.extensions.utils.env
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import joptsimple.OptionSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import me.obsilabor.obsibot.commands.GiveawayCommand
import me.obsilabor.obsibot.commands.PollCommand
import me.obsilabor.obsibot.config.ConfigManager
import me.obsilabor.obsibot.listeners.GiveawayListener
import me.obsilabor.obsibot.listeners.PingListener
import me.obsilabor.obsibot.listeners.ReadyListener
import me.obsilabor.obsibot.localization.Localization
import me.obsilabor.obsibot.localization.globalText
import me.obsilabor.obsibot.tasks.GiveawayTask
import java.util.*

object ObsiBot {

    lateinit var bot: ExtensibleBot
    lateinit var client: Kord
    var isFullyFunctional = false
    var initTime: Long = 0

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

    @KordPreview
    suspend fun main(optionSet: OptionSet) {
        initTime = System.currentTimeMillis()
        ConfigManager.mongoConfig
        Localization.extractLanguageFiles()
        Localization.loadAllLanguageFiles()
        if(optionSet.has("setup")) {
            globalText("bot.help", hashMapOf("languages" to Localization.languages.keys)).split("\\n").forEach {
                println(it)
            }
            return
        }
        if(optionSet.hasArgument("language")) {
            Localization.globalLanguage = optionSet.valueOf("language").toString()
        }
        val timer = Timer()
        timer.schedule(GiveawayTask(), 0, 1000)
        bot = ExtensibleBot(TOKEN) {
            extensions {
                add(::PingListener)
                add(::ReadyListener)
                add(::GiveawayListener)
                add(::GiveawayCommand)
                add(::PollCommand)
            }
        }
        bot.start()
    }
}