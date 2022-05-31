package com.truckersmp.api

import kotlin.Boolean
import kotlin.Double
import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class Ban(
  /**
   * The time the ban will expire
   */
  public val expiration: String,
  /**
   * The time the ban was issued
   */
  public val timeAdded: String,
  /**
   * If the ban is still active
   */
  public val active: Boolean,
  /**
   * The reason for the ban
   */
  public val reason: String,
  /**
   * Name of the admin that banned the user
   */
  public val adminName: String,
  /**
   * TruckersMP ID for the admin that banned the user
   */
  public val adminID: Double,
)
