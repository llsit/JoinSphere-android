package com.llsit.joinsphere.core.model.event

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime

@Serializable
data class AttendingEvent @OptIn(ExperimentalTime::class) constructor(
    val id: String,
    val title: String,
    val coverImage: String,
    val categoryId: String,

    val hostName: String,
    val hostAvatar: String?,

    val address: String,

    val attendeeCount: Int,
    val maxAttendeeCount: Int?,

    val isFree: Boolean,
    val price: Double,

    val startTimestamp: Instant,
    val endTimestamp: Instant?,

    val unreadCount: Int,

    val eventState: EventState
)
