package com.llsit.joinsphere.core.model.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
data class SavedEventDto @OptIn(ExperimentalTime::class) constructor(
    @SerialName("id")
    val id: String,

    @SerialName("title")
    val title: String,

    @SerialName("cover_image_url")
    val coverImageUrl: String,

    @SerialName("category_id")
    val categoryId: String,

    @SerialName("address")
    val address: String,

    @SerialName("attendee_count")
    val attendeeCount: Int,

    @SerialName("max_attendees")
    val maxAttendees: Int?,

    @SerialName("is_free")
    val isFree: Boolean,

    @SerialName("price")
    val price: Double,

    @SerialName("start_timestamp")
    val startTimestamp: Instant,

    @SerialName("end_timestamp")
    val endTimestamp: Instant?
)