package me.obsilabor.obsibot.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.TimeZone.Companion.UTC
import kotlinx.datetime.plus
import kotlin.time.DurationUnit

public fun DateTimePeriod.toTotalSeconds(): Int {
    val now = Clock.System.now()
    return (now.plus(this, UTC) - now).toInt(DurationUnit.SECONDS)
}

public operator fun DateTimePeriod.compareTo(other: DateTimePeriod): Int =
    this.toTotalSeconds().compareTo(other.toTotalSeconds())