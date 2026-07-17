package com.llsit.joinsphere.core.domain.usecase

import com.llsit.joinsphere.core.domain.repository.EventRepository
import com.llsit.joinsphere.core.domain.repository.UserDataRepository
import com.llsit.joinsphere.core.model.event.SavedEventDto

class GetSavedEventsUseCase(
    private val eventRepository: EventRepository,
    private val userDataRepository: UserDataRepository
) {
    suspend operator fun invoke(): Result<List<SavedEventDto>> {
        val userId = userDataRepository.currentUserId
            ?: return Result.failure(Exception("User not logged in"))
        return eventRepository.getSavedEvents(userId)
    }
}
