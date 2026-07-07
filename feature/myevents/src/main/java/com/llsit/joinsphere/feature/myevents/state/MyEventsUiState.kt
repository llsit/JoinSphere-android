package com.llsit.joinsphere.feature.myevents.state

import com.llsit.joinsphere.core.model.event.AttendingEvent
import com.llsit.joinsphere.core.model.event.EventDto

data class MyEventsUiState(
    val todayEvents: List<AttendingEvent> = emptyList(),
    val tomorrowEvents: List<AttendingEvent> = emptyList(),
    val thisWeekEvents: List<AttendingEvent> = emptyList(),
    val laterEvents: List<AttendingEvent> = emptyList(),
    val hostingEvents: List<EventDto> = emptyList(),
    val pastEvents: List<AttendingEvent> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
