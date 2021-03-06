package me.obsilabor.obsibot.data

import dev.kord.common.entity.Snowflake

@kotlinx.serialization.Serializable
data class Giveaway(
    val owner: Snowflake,
    var participants: ArrayList<Snowflake> = arrayListOf(),
    val messageId: Snowflake,
    val channelId: Snowflake,
    val guildId: Snowflake,
    val end: Long,
    val prize: String,
    val prizeCount: Int,
    var rolled: Boolean
) {

    fun roll(): List<Snowflake> {
        val winners = arrayListOf<Snowflake>()
        repeat(prizeCount) {
            val winner = participants.random()
            if(!winners.contains(winner)) {
                winners.add(winner)
            }
        }
        rolled = true
        return winners
    }

    fun addNewUser(user: Snowflake): Giveaway {
        participants.add(user)
        return this
    }
}
