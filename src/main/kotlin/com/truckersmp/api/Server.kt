package com.truckersmp.api

import kotlin.Boolean
import kotlin.Double
import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class Server(
  /**
   * The ID given to the server
   */
  public val id: Double,
  /**
   * What game the server is for (ETS2 or ATS)
   */
  public val game: String,
  /**
   * The server ip address
   */
  public val ip: String,
  /**
   * The port that the server runs on
   */
  public val port: Double,
  /**
   * Name of the server
   */
  public val name: String,
  /**
   * Shortname for the server
   */
  public val shortname: String,
  /**
   * Shown in-game in front of a player's ID
   */
  public val idprefix: String? = null,
  /**
   * If the server is online or not
   */
  public val online: Boolean,
  /**
   * How many players are currently on the server
   */
  public val players: Double,
  /**
   * Amount of players waiting in the queue to join the server
   */
  public val queue: Double,
  /**
   * The max amount of players allowed on the server at once
   */
  public val maxplayers: Double,
  /**
   * The map ID given to the server used by ETS2Map
   */
  public val mapid: Double,
  /**
   * Determines the order in which servers are displayed
   */
  public val displayorder: Double,
  /**
   * If the speed limiter is enabled on the server (110 kmh for ETS2 and 80 mph for ATS)
   */
  public val speedlimiter: Double,
  /**
   * If server wide collisions is enabled
   */
  public val collisions: Boolean,
  /**
   * If cars are enabled for players
   */
  public val carsforplayers: Boolean,
  /**
   * If police cars can be driven by players
   */
  public val policecarsforplayers: Boolean,
  /**
   * If AFK kick is enabled for players
   */
  public val afkenabled: Boolean,
  /**
   * If the server is an event server
   */
  public val event: Boolean,
  /**
   * Determine whether the server hosts special event files (e.g. map edits, special cargos or new
   * paint jobs)
   */
  public val specialEvent: Boolean,
  /**
   * Determine whether the server hosts ProMods
   */
  public val promods: Boolean,
  /**
   * Server tick rate
   */
  public val syncdelay: Double,
)
