package com.truckersmp.api

import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class Player(
  /**
   * The ID of the user requested
   */
  public val id: Double,
  /**
   * The name of the user
   */
  public val name: String,
  /**
   * URL to the avatar used on the website
   */
  public val avatar: String,
  /**
   * URL to the avatar used on the website (32px x 32px)
   */
  public val smallAvatar: String,
  /**
   * The date and time the user registered (UTC)
   */
  public val joinDate: String,
  /**
   * The SteamID64 of the user
   */
  public val steamID64: Double,
  /**
   * The SteamID64 of the user
   */
  public val steamID: String,
  /**
   * The Discord account linked to the user or null if not linked or private
   */
  public val discordSnowflake: String? = null,
  /**
   * Is the user's VTC History visible
   */
  public val displayVTCHistory: Boolean,
  /**
   * The name of the group the user belongs to
   */
  public val groupName: String,
  /**
   * The color of the group
   */
  public val groupColor: String,
  /**
   * The ID of the group the user belongs to
   */
  public val groupID: Double,
  /**
   * If the user is currently banned
   */
  public val banned: Boolean,
  /**
   * The date and time the ban will expire (UTC) or `null` if not banned or ban is permanent
   */
  public val bannedUntil: String? = null,
  /**
   * The number of active bans a user has or `null` if staff
   */
  public val bansCount: Int? = null,
  /**
   * If the user has their bans hidden
   */
  public val displayBans: Boolean,
  public val patreon: Player.Patreon,
  public val permissions: Player.Permissions,
  public val vtc: Player.Vtc,
) {
  @Serializable
  public data class Patreon(
    /**
     * If the user has donated or is currently donating via Patreon
     */
    public val isPatron: Boolean,
    /**
     * If the user has an active Patreon subscription
     */
    public val active: Boolean,
    /**
     * HEX code for subscribed tier
     */
    public val color: String? = null,
    /**
     * The tier ID of current pledge
     */
    public val tierId: Int? = null,
    /**
     * Current pledge in cents
     */
    public val currentPledge: Int? = null,
    /**
     * Lifetime pledge in cents
     */
    public val lifetimePledge: Int? = null,
    /**
     * Next pledge in cents
     */
    public val nextPledge: Int? = null,
    /**
     * If user has their Patreon information hidden
     */
    public val hidden: Boolean? = null,
  )

  @Serializable
  public data class Permissions(
    /**
     * If the user is a TruckersMP staff member
     */
    public val isStaff: Boolean,
    /**
     * If the user is part of upper staff within the TruckersMP team
     */
    public val isUpperStaff: Boolean,
    /**
     * If the user has Game Moderator permissions
     */
    public val isGameAdmin: Boolean,
    /**
     * Always false
     */
    public val showDetailedOnWebMaps: Boolean,
  )

  @Serializable
  public data class Vtc(
    /**
     * ID of the VTC the user belongs to or 0 if not in a VTC
     */
    public val id: Double,
    /**
     * Name of the VTC the user belongs to or empty if not in a VTC
     */
    public val name: String,
    /**
     * Tag of the VTC the user belongs to or empty if not in a VTC
     */
    public val tag: String,
    /**
     * If the user is in a VTC
     */
    public val inVTC: Boolean,
    /**
     * VTC member ID or 0 if not in a VTC
     */
    public val memberID: Double,
  )
}
