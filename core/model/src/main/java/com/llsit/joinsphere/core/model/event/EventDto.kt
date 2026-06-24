package com.llsit.joinsphere.core.model.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventDto(
    @SerialName("id")
    val id: String? = null,
    @SerialName("title")
    val title: String,
    @SerialName("category_id")
    val categoryId: String? = "",
    @SerialName("description")
    val description: String? = "",
    @SerialName("cover_image_url")
    val coverImageUrl: String? = "",
    @SerialName("date")
    val date: String,
    @SerialName("time")
    val time: String,
    @SerialName("address")
    val address: String,
    @SerialName("latitude")
    val latitude: Double,
    @SerialName("longitude")
    val longitude: Double,
    @SerialName("max_attendees")
    val maxAttendees: Int? = null,
    @SerialName("is_free")
    val isFree: Boolean? = true,
    @SerialName("price")
    val price: Double? = 0.0,
    @SerialName("is_solo_friendly")
    val isSoloFriendly: Boolean? = true,
    @SerialName("creator_id")
    val creatorId: String,
    @SerialName("created_at")
    val createdAt: Long? = null,
    @SerialName("attendee_count")
    val attendeeCount: Int? = 1,
    @SerialName("start_timestamp")
    val startTimestamp: String
)