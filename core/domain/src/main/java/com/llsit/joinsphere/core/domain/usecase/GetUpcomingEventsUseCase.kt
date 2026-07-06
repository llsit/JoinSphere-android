package com.llsit.joinsphere.core.domain.usecase

import com.llsit.joinsphere.core.domain.repository.EventRepository
import com.llsit.joinsphere.core.model.event.AttendingEvent

class GetUpcomingEventsUseCase(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(): Result<List<AttendingEvent>> {
        return eventRepository.getUpcomingEvents()
    }
}
