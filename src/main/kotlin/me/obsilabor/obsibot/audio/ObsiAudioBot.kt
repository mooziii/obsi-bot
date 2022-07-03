package me.obsilabor.obsibot.audio

import com.kotlindiscord.kord.extensions.utils.hasPermission
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import dev.kord.common.annotation.KordPreview
import dev.kord.common.annotation.KordVoice
import dev.kord.common.entity.Permission
import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.Guild
import dev.kord.core.entity.Member
import dev.kord.core.entity.channel.VoiceChannel
import dev.kord.gateway.editPresence
import dev.kord.voice.AudioFrame
import dev.kord.voice.VoiceConnection
import me.obsilabor.obsibot.ObsiBot
import me.obsilabor.obsibot.config.RadioStreamConfig
import me.obsilabor.obsibot.localization.localText
import me.obsilabor.obsibot.utils.createObsiGuild
import me.obsilabor.obsibot.utils.obsify
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@KordPreview
@KordVoice
object ObsiAudioBot {

    private val connections: MutableMap<Snowflake, VoiceConnection> = mutableMapOf()
    private lateinit var lavaplayerManager: DefaultAudioPlayerManager

    fun setupAudio() {
        lavaplayerManager = DefaultAudioPlayerManager()
        AudioSourceManagers.registerRemoteSources(lavaplayerManager)

    }

    suspend fun disconnect(guild: Guild?) {
        if(guild == null) return
        connections[guild.id]?.shutdown()
        connections.remove(guild.id)
        guild.gateway?.editPresence {
            playing("Give us a star on Github!")
        }
    }

    suspend fun playRadioStream(url: String, guild: Guild, member: Member, channel: VoiceChannel, radioName: String, radioStreamConfig: RadioStreamConfig): String {
        if(connections[guild.id] != null) {
            if(member.hasPermission(Permission.MoveMembers)) {
                disconnect(guild)
            } else {
                return localText("command.radio.failure.alreadyconnected", guild.obsify() ?: guild.createObsiGuild())
            }
        }
        val player = lavaplayerManager.createPlayer()
        lavaplayerManager.playTrack(url, player)
        val connection = channel.connect {
            audioProvider { AudioFrame.fromData(player.provide()?.data) }
        }
        connections[guild.id] = connection
        guild.gateway?.editPresence {
            listening("$radioName 🎶")
        }
        return localText("command.radio.success", hashMapOf("radio" to radioName, "voicechannel" to channel.id.value, "emoji" to radioStreamConfig.flagEmoji), guild.obsify() ?: guild.createObsiGuild())
    }

    private suspend fun DefaultAudioPlayerManager.playTrack(query: String, player: AudioPlayer): AudioTrack {
        val track = suspendCoroutine<AudioTrack> {
            this.loadItem(query, object : AudioLoadResultHandler {
                override fun trackLoaded(track: AudioTrack) {
                    it.resume(track)
                }

                override fun playlistLoaded(playlist: AudioPlaylist) {
                    it.resume(playlist.tracks.first())
                }

                override fun noMatches() {
                    println("No match?")
                }

                override fun loadFailed(exception: FriendlyException?) {
                    exception?.printStackTrace()
                }
            })
        }

        player.playTrack(track)
        return track
    }

}