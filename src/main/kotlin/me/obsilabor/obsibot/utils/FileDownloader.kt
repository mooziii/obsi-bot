package me.obsilabor.obsibot.utils

import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.util.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import kotlinx.coroutines.*
import java.io.File
import kotlin.math.roundToInt
import me.obsilabor.obsibot.ObsiBot

object FileDownloader {

    @OptIn(InternalAPI::class)
    suspend fun downloadFile(url: String, targetFile: File, current: Int, total: Int) {
        val downloadContent = ObsiBot.ktorClient.get(url) {
            onDownload { bytesSentTotal, contentLength ->
                val progress = bytesSentTotal.toDouble() / contentLength.toDouble()
                val hashtags = (progress * 30).roundToInt()
                val percentage = (progress * 100).roundToInt()
                ObsiBot.generalScope.launch(Dispatchers.IO) {
                    val string = buildString {
                        append('[')
                        repeat(hashtags) {
                            append("#")
                        }
                        repeat(30 - hashtags) {
                            append(' ')
                        }
                        append("] ${percentage}%")
                        append(" " + targetFile.name + " ($current/$total)")
                    }
                    print("\r  $string")
                }.join()
            }
        }.readBytes()
        println()
        targetFile.writeBytes(downloadContent)
    }

    suspend fun downloadFile(url: String, targetFile: File) {
        downloadFile(url, targetFile, 1, 1)
    }

}