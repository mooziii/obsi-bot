package com.truckersmp.api

import kotlin.Boolean
import kotlin.Double
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class VTCNews(
  /**
   * The ID of the article
   */
  public val id: Double,
  /**
   * The title of the article
   */
  public val title: String,
  /**
   * A summary of the article
   */
  @SerialName("content_summary")
  public val contentSummary: String,
  /**
   * The article's content in Markdown
   */
  public val content: String,
  /**
   * The user's ID who made the article
   */
  @SerialName("author_id")
  public val authorId: Double,
  /**
   * The user's name who made the article
   */
  public val author: String,
  /**
   * If the article is pinned
   */
  public val pinned: Boolean,
  /**
   * The date and time the article was updated at (UTC)
   */
  @SerialName("updated_at")
  public val updatedAt: String,
  /**
   * The date and time the article was published at (UTC)
   */
  @SerialName("published_at")
  public val publishedAt: String,
)
