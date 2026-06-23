package com.llsit.joinsphere.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiscoverFeedsResponse(
    @SerialName("trending")
    val trending: List<EventNetworkModel>,
    @SerialName("thisWeek")
    val thisWeek: List<EventNetworkModel>
)

@Serializable
data class EventNetworkModel(
    val id: String,
    val title: String,
    @SerialName("category_id")
    val category: String? = null,
    @SerialName("cover_image_url")
    val coverImageUrl: String? = null,
    val score: Double = 0.0,
    @SerialName("attendee_count")
    val attendeesCount: Int = 0,
    @SerialName("start_timestamp")
    val startTimestamp: String? = null
)