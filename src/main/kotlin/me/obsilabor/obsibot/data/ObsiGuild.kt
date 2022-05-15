package me.obsilabor.obsibot.data

import dev.kord.common.entity.Snowflake
import kotlinx.coroutines.launch
import me.obsilabor.obsibot.ObsiBot
import me.obsilabor.obsibot.database.MongoManager
import me.obsilabor.obsibot.localization.Localization
import org.litote.kmongo.eq

@kotlinx.serialization.Serializable
data class ObsiGuild(
    var documentVersion: Int,
    val id: Snowflake,
    var language: String,
    var giveaways: ArrayList<Giveaway>,
    var giveawayRole: Snowflake?,
    var polls: ArrayList<Poll>?,
    var pollRole: Snowflake?
) {

    companion object {
        const val NEWEST_DOCUMENT_VERSION = 1

        fun newDocument(snowflake: Snowflake): ObsiGuild {
            return ObsiGuild(
                NEWEST_DOCUMENT_VERSION,
                snowflake,
                Localization.DEFAULT_LANGUAGE,
                arrayListOf(),
                null,
                arrayListOf(),
                null
            )
        }
    }

    fun update() {
        ObsiBot.generalScope.launch {
            MongoManager.guilds.replaceOne(ObsiGuild::id eq id, this@ObsiGuild)
        }
    }

    fun migrateIfNeeded(): ObsiGuild {
        if(documentVersion < NEWEST_DOCUMENT_VERSION) {
            documentVersion = NEWEST_DOCUMENT_VERSION
            if(polls == null) {
                polls = arrayListOf()
            }
            update()
        }
        return this
    }

    fun adoptLanguage(newLanguage: String): ObsiGuild {
        language = newLanguage
        return this
    }

    fun adoptNewGiveaway(giveaway: Giveaway): ObsiGuild {
        giveaways.add(giveaway)
        return this
    }

    fun adoptGiveaways(newGiveaways: ArrayList<Giveaway>): ObsiGuild {
        giveaways = newGiveaways
        return this
    }

    fun adoptGiveawayRoleId(newId: Snowflake?): ObsiGuild {
        giveawayRole = newId
        return this
    }

    fun adoptPollRoleId(newId: Snowflake?): ObsiGuild {
        pollRole = newId
        return this
    }

    fun adoptNewPoll(newPoll: Poll): ObsiGuild {
        polls?.add(newPoll)
        return this
    }

    fun refreshPollVotes(newPoll: Poll): ObsiGuild {
        val newList = arrayListOf(newPoll)
        polls?.forEach {
            if(it.messageId != newPoll.messageId) {
                newList.add(it)
            }
        }
        polls = newList
        return this
    }

}
