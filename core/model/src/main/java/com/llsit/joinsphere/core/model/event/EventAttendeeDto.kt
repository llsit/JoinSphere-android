package com.llsit.joinsphere.core.model.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventAttendeeDto(
    @SerialName("id")
    val id: String? = null,
    
    @SerialName("event_id")
    val eventId: String,
    
    @SerialName("user_id")
    val userId: String,
    
    @SerialName("joined_at")
    val joinedAt: String? = null
)
