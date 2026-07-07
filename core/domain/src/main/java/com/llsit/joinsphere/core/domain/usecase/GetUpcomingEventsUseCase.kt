package com.llsit.joinsphere.core.domain.usecase

import com.llsit.joinsphere.core.domain.repository.EventRepository
import com.llsit.joinsphere.core.model.event.MyEventsResponse

class GetUpcomingEventsUseCase(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(): Result<MyEventsResponse> {
        return eventRepository.getUpcomingEvents()
    }
}
