package me.obsilabor.obsibot.data

import dev.kord.common.entity.Snowflake

@kotlinx.serialization.Serializable
data class Poll(
    val guildId: Snowflake,
    val channelId: Snowflake,
    val interactionId: String,
    val messageId: Snowflake,
    val owner: Snowflake,
    val options: HashMap<String, Int>,
    val end: Long,
    val voters: HashMap<Snowflake, String>,
    val ended: Boolean
) {

    val winner: String
    get() = options.toList().maxByOrNull { it.second }!!.first
}
