package me.obsilabor.obsibot.data

import dev.kord.common.entity.Snowflake

@kotlinx.serialization.Serializable
data class Poll(
    val interactionId: String,
    val messageId: Snowflake,
    val owner: Snowflake,
    val options: HashMap<String, Int>,
    val end: Long
)
