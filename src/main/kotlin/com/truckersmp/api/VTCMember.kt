package com.truckersmp.api

import kotlin.Boolean
import kotlin.Double
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class VTCMember(
  /**
   * The ID of the VTC Member
   */
  public val id: Double,
  /**
   * The member's TruckersMP ID
   */
  @SerialName("user_id")
  public val userId: Double,
  /**
   * The member's username
   */
  public val username: String,
  /**
   * The member's STEAM_ID
   */
  @SerialName("steam_id")
  public val steamId: Double,
  /**
   * The member's role ID
   */
  @SerialName("role_id")
  public val roleId: Double,
  /**
   * The member's role name
   */
  public val role: String,
  /**
   * If the member has owner permissions
   */
  @SerialName("is_owner")
  public val isOwner: Boolean? = null,
  /**
   * The date and time the member joined (UTC)
   */
  public val joinDate: String,
)
