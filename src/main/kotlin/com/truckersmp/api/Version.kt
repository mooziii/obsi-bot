package com.truckersmp.api

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class Version(
  /**
   * Name of the current version
   */
  public val name: String,
  /**
   * Numeric name of the current version
   */
  public val numeric: String,
  /**
   * Current stage in the development process
   */
  public val stage: String,
  @SerialName("ets2mp_checksum")
  public val ets2mpChecksum: Version.Ets2mpChecksum,
  @SerialName("atsmp_checksum")
  public val atsmpChecksum: Version.AtsmpChecksum,
  /**
   * The time that the version was released
   */
  public val time: String,
  /**
   * ETS2 version that is supported
   */
  @SerialName("supported_game_version")
  public val supportedGameVersion: String,
  /**
   * ATS version that is supported
   */
  @SerialName("supported_ats_game_version")
  public val supportedAtsGameVersion: String,
) {
  @Serializable
  public data class Ets2mpChecksum(
    /**
     * Checksum of core.dll
     */
    public val dll: String,
    /**
     * Checksum of data1.adb
     */
    public val adb: String,
  )

  @Serializable
  public data class AtsmpChecksum(
    /**
     * Checksum of core.dll
     */
    public val dll: String,
    /**
     * Checksum of data1.adb
     */
    public val adb: String,
  )
}
