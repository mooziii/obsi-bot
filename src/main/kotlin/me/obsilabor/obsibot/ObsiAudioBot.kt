package me.obsilabor.obsibot

import com.kotlindiscord.kord.extensions.utils.hasPermission
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import dev.kord.common.entity.Permission
import dev.kord.common.entity.Permissions
import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.Guild
import dev.kord.core.entity.Member
import dev.kord.core.entity.channel.VoiceChannel
import dev.kord.voice.AudioFrame
import dev.kord.voice.VoiceConnection
import me.obsilabor.obsibot.localization.localText
import me.obsilabor.obsibot.utils.createObsiGuild
import me.obsilabor.obsibot.utils.obsify
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object ObsiAudioBot {

    val connections: MutableMap<Snowflake, VoiceConnection> = mutableMapOf()
    private lateinit var lavaplayerManager: DefaultAudioPlayerManager

    fun setupAudio() {
        lavaplayerManager = DefaultAudioPlayerManager()
        AudioSourceManagers.registerRemoteSources(lavaplayerManager)

    }

    suspend fun disconnect(guild: Guild) {
        connections[guild.id]?.shutdown()
        ObsiBot.client.editPresence {
            playing("Give us a star on Github!")
        }
    }

    suspend fun playRadioStream(url: String, guild: Guild, member: Member, channel: VoiceChannel, radioName: String): String {
        if(connections[guild.id] != null) {
            if(member.hasPermission(Permission.MoveMembers)) {
                disconnect(guild)
            } else {
                return localText("command.radio.failure.alreadyconnected", guild.obsify() ?: guild.createObsiGuild())
            }
        }
        val player = lavaplayerManager.createPlayer()
        val track = lavaplayerManager.playTrack(url, player)
        val connection = channel.connect {
            audioProvider { AudioFrame.fromData(player.provide()?.data) }
        }
        println(track.info.title)
        ObsiBot.client.editPresence {
            listening("$radioName 🎶")
        }
        connections[guild.id] = connection
        return localText("command.radio.success", hashMapOf("radio" to radioName, "voicechannel" to channel.id.value), guild.obsify() ?: guild.createObsiGuild())
    }

    suspend fun DefaultAudioPlayerManager.playTrack(query: String, player: AudioPlayer): AudioTrack {
        val track = suspendCoroutine<AudioTrack> {
            this.loadItem(query, object : AudioLoadResultHandler {
                override fun trackLoaded(track: AudioTrack) {
                    it.resume(track)
                }

                override fun playlistLoaded(playlist: AudioPlaylist) {
                    it.resume(playlist.tracks.first())
                }

                override fun noMatches() {
                    TODO()
                }

                override fun loadFailed(exception: FriendlyException?) {
                    TODO()
                }
            })
        }

        player.playTrack(track)

        return track
    }

}