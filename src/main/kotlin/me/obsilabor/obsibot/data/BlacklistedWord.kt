package me.obsilabor.obsibot.data

@kotlinx.serialization.Serializable
data class BlacklistedWord(
    val word: String,
    val why: String,
    val checkVariants: Boolean = true
)