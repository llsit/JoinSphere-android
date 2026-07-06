package com.llsit.joinsphere.feature.myevents.state

import com.llsit.joinsphere.core.model.event.AttendingEvent
import com.llsit.joinsphere.core.model.event.EventDto

data class MyEventsUiState(
    val upcomingEvents: List<AttendingEvent> = emptyList(),
    val hostingEvents: List<EventDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
