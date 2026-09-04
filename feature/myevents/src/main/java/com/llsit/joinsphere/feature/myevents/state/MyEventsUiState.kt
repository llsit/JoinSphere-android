package com.llsit.joinsphere.feature.myevents.state

import com.llsit.joinsphere.core.model.event.AttendingEvent
import com.llsit.joinsphere.core.model.event.EventDto
import com.llsit.joinsphere.core.model.event.SavedEventDto

data class MyEventsUiState(
    val todayEvents: List<AttendingEvent> = emptyList(),
    val tomorrowEvents: List<AttendingEvent> = emptyList(),
    val thisWeekEvents: List<AttendingEvent> = emptyList(),
    val laterEvents: List<AttendingEvent> = emptyList(),
    val hostingEvents: List<EventDto> = emptyList(),
    val pastEvents: List<AttendingEvent> = emptyList(),
    val savedEvents: List<SavedEventDto> = emptyList(),
    val isLoadingUpcoming: Boolean = false,
    val isLoadingHosting: Boolean = false,
    val isLoadingSaved: Boolean = false,
    val isLoadingPast: Boolean = false,
    val error: String? = null
) {
    val isLoading: Boolean
        get() = isLoadingUpcoming || isLoadingHosting || isLoadingSaved || isLoadingPast
}
