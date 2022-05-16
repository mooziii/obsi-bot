package me.obsilabor.obsibot.utils

object PercentageUtils {

    fun toString(percentage: Double): String {
        return (percentage * 100).toString().split(".")[0]
    }

}