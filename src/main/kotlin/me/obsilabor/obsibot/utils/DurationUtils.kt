package me.obsilabor.obsibot.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.TimeZone.Companion.UTC
import kotlinx.datetime.plus
import kotlin.time.Duration
import kotlin.time.DurationUnit

fun DateTimePeriod.toTotalSeconds(): Int {
    val now = Clock.System.now()
    return (now.plus(this, UTC) - now).toInt(DurationUnit.SECONDS)
}

fun DateTimePeriod.string(): String {
    return buildString {
        if(days > 0) {
            append("${days}d ")
        }
        if(hours > 0) {
            append("${hours}h ")
        }
        if(minutes > 0) {
            append("${minutes}m ")
        }
        append("${seconds}s")
    }
}

operator fun DateTimePeriod.compareTo(other: DateTimePeriod): Int =
    this.toTotalSeconds().compareTo(other.toTotalSeconds())