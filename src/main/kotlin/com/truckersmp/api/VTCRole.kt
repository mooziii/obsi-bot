package com.truckersmp.api

import kotlin.Boolean
import kotlin.Double
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class VTCRole(
  /**
   * The ID of the role
   */
  public val id: Double,
  /**
   * The name of the role
   */
  public val name: String,
  /**
   * The current position of the role
   */
  public val order: Double,
  /**
   * If the role has owner permissions
   */
  public val owner: Boolean,
  /**
   * The date and time the role was created (UTC)
   */
  @SerialName("created_at")
  public val createdAt: String,
  /**
   * The date and time the role was updated at (UTC)
   */
  @SerialName("updated_at")
  public val updatedAt: String,
)
