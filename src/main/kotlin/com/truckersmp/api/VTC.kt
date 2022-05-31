package com.truckersmp.api

import kotlin.Boolean
import kotlin.Double
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class VTC(
  /**
   * The ID of the VTC
   */
  public val id: Double,
  /**
   * The name of the VTC
   */
  public val name: String,
  /**
   * The owner's ID of the VTC
   */
  @SerialName("owner_id")
  public val ownerId: Double,
  /**
   * The owner's username of the VTC
   */
  @SerialName("owner_username")
  public val ownerUsername: String,
  /**
   * The VTC's slogan
   */
  public val slogan: String,
  /**
   * The VTC's tag
   */
  public val tag: String,
  /**
   * URL to the logo used on the website
   */
  public val logo: String,
  /**
   * URL to the cover photo used on the website
   */
  public val cover: String,
  /**
   * The VTC's information in Markdown
   */
  public val information: String,
  /**
   * The VTC's rules in Markdown
   */
  public val rules: String,
  /**
   * The VTC's requirements in Markdown
   */
  public val requirements: String,
  /**
   * URL to the VTC's website
   */
  public val website: String? = null,
  public val socials: VTC.Socials,
  public val games: VTC.Games,
  /**
   * The VTC's member count
   */
  @SerialName("members_count")
  public val membersCount: Double,
  /**
   * If the VTC is accepting new members (`Close` | `Open`)
   */
  public val recruitment: String,
  /**
   * The VTC's main language
   */
  public val language: String,
  /**
   * If the VTC is verified
   */
  public val verified: Boolean,
  /**
   * If the VTC is validated
   */
  public val validated: Boolean,
  /**
   * The date and time the VTC was created at (UTC)
   */
  public val created: String,
) {
  @Serializable
  public data class Socials(
    /**
     * URL to the VTC's Twitter
     */
    public val twitter: String? = null,
    /**
     * URL to the VTC's Facebook
     */
    public val facebook: String? = null,
    /**
     * URL to the VTC's Twitch
     */
    public val twitch: String? = null,
    /**
     * URL to the VTC's Discord
     */
    public val discord: String? = null,
    /**
     * URL to the VTC's Youtube
     */
    public val youtube: String? = null,
  )

  @Serializable
  public data class Games(
    /**
     * If the VTC allows ATS
     */
    public val ats: Boolean,
    /**
     * If the VTC allows ETS2
     */
    public val ets: Boolean,
  )
}
