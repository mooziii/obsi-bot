package com.truckersmp.api

import kotlin.Double
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class EventIndex(
  /**
   * The event's ID
   */
  public val id: Double,
  /**
   * The event's type
   */
  @SerialName("event_type")
  public val eventType: EventIndex.EventType,
  /**
   * The event's name
   */
  public val name: String,
  /**
   * The event's slug
   */
  public val slug: String,
  /**
   * The event's game (`ATS` | `ETS2`)
   */
  public val game: String,
  /**
   * The event's server information
   */
  public val server: EventIndex.Server,
  /**
   * The event's main language
   */
  public val language: String,
  public val departure: EventIndex.Departure,
  public val arrive: EventIndex.Arrive,
  /**
   * The date and time the event starts at (UTC)
   */
  @SerialName("start_at")
  public val startAt: String,
  /**
   * URL to the banner used on the website
   */
  public val banner: String,
  /**
   * URL to the map used on the website
   */
  public val map: String? = null,
  /**
   * The event's description in Markdown
   */
  public val description: String,
  /**
   * The event's rules in Markdown
   */
  public val rule: String? = null,
  /**
   * URL to the event's voice location
   */
  @SerialName("voice_link")
  public val voiceLink: String? = null,
  /**
   * external URL specified for the event
   */
  @SerialName("external_link")
  public val externalLink: String? = null,
  /**
   * If the event is featured
   */
  public val featured: String,
  /**
   * If the Event is hosted by a VTC
   */
  public val vtc: EventIndex.Vtc? = null,
  public val user: EventIndex.User,
  public val attendances: EventIndex.Attendances,
  /**
   * The event's required DLCs
   */
  public val dlcs: EventIndex.Dlcs,
  /**
   * URL to the event
   */
  public val url: String,
  /**
   * The date and time the event was created at (UTC)
   */
  @SerialName("created_at")
  public val createdAt: String,
  /**
   * The date and time the event was updated at (UTC)
   */
  @SerialName("updated_at")
  public val updatedAt: String,
) {
  @Serializable
  public data class EventType(
    /**
     * The event's type key
     */
    public val key: String,
    /**
     * The event's type name
     */
    public val name: String,
  )

  @Serializable
  public data class Server(
    /**
     * The event's server ID
     */
    public val id: Double,
    /**
     * The event's server name
     */
    public val name: String,
  )

  @Serializable
  public data class Departure(
    /**
     * The event's departure location
     */
    public val location: String,
    /**
     * The event's departure city
     */
    public val city: String,
  )

  @Serializable
  public data class Arrive(
    /**
     * The event's arrival location
     */
    public val location: String,
    /**
     * The event's arrival city
     */
    public val city: String,
  )

  @Serializable
  public data class Vtc(
    /**
     * The VTC's ID
     */
    public val id: Double,
    /**
     * The VTC's Name
     */
    public val name: String,
  )

  @Serializable
  public data class User(
    /**
     * The event's creator's ID
     */
    public val id: Double,
    /**
     * The event's creator's username
     */
    public val username: String,
  )

  @Serializable
  public data class Attendances(
    /**
     * The number of confirmed attendees
     */
    public val confirmed: Double,
    /**
     * The number of unsure attendees
     */
    public val unsure: Double,
  )

  @Serializable
  public data class Dlcs(
    /**
     * The DLCs id followed by the DLCs name
     */
    @SerialName("dlc_id")
    public val dlcId: String,
  )
}
