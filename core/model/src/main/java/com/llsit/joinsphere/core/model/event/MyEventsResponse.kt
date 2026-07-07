package com.llsit.joinsphere.core.model.event

import kotlinx.serialization.Serializable

@Serializable
data class MyEventsResponse(
    val today: List<AttendingEvent> = emptyList(),
    val tomorrow: List<AttendingEvent> = emptyList(),
    val thisWeek: List<AttendingEvent> = emptyList(),
    val later: List<AttendingEvent> = emptyList()
)
