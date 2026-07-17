package com.llsit.joinsphere.core.model.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FavoriteDto(
    @SerialName("event_id")
    val eventId: String
)
