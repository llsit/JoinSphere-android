package com.llsit.joinsphere.core.model

data class EventDto(
    val id: String = "",
    val title: String = "",
    val categoryId: String = "",
    val description: String = "",
    val coverImageUrl: String = "",
    val date: String = "",
    val time: String = "",
    val location: String = "",
    val maxAttendees: Int? = null,
    val isFree: Boolean = true,
    val price: Double = 0.0,
    val isSoloFriendly: Boolean = true,
    val creatorId: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val attendeeCount: Int = 1,
)