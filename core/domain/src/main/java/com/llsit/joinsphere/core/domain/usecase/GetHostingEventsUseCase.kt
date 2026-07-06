package com.llsit.joinsphere.core.domain.usecase

import com.llsit.joinsphere.core.domain.repository.EventRepository
import com.llsit.joinsphere.core.domain.repository.UserDataRepository
import com.llsit.joinsphere.core.model.event.EventDto

class GetHostingEventsUseCase(
    private val eventRepository: EventRepository,
    private val userDataRepository: UserDataRepository
) {
    suspend operator fun invoke(): Result<List<EventDto>> {
        val userId = userDataRepository.currentUserId
            ?: return Result.failure(Exception("User not logged in"))
        
        return eventRepository.getHostingEvents(userId)
    }
}
