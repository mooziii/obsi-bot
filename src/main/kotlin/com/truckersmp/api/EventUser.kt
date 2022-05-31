package com.truckersmp.api

import kotlin.Boolean
import kotlin.Double
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class EventUser(
  /**
   * The event user's ID
   */
  public val id: Double,
  /**
   * The event user's username
   */
  public val username: String,
  /**
   * Always false
   */
  public val following: Boolean,
  /**
   * The date and time the user marked their attendance at (UTC)
   */
  @SerialName("created_at")
  public val createdAt: String,
  /**
   * The date and time the user updated their attendance at (UTC)
   */
  @SerialName("updated_at")
  public val updatedAt: String,
)
