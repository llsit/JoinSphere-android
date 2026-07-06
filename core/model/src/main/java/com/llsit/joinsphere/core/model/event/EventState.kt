package com.llsit.joinsphere.core.model.event

import kotlinx.serialization.Serializable

@Serializable
enum class EventState {
    UPCOMING,
    STARTING_SOON,
    LIVE,
    ENDED
}
