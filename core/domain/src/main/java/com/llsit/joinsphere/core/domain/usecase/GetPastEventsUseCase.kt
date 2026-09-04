package com.llsit.joinsphere.core.domain.usecase

import com.llsit.joinsphere.core.domain.repository.EventRepository
import com.llsit.joinsphere.core.domain.repository.UserDataRepository
import com.llsit.joinsphere.core.model.event.AttendingEvent

class GetPastEventsUseCase(
    private val repository: EventRepository,
    private val userDataRepository: UserDataRepository
) {

    suspend operator fun invoke(): Result<List<AttendingEvent>> {

        val userId = userDataRepository.currentUserId
            ?: return Result.failure(Exception("User not logged in"))

        return repository.getPastEvents(userId)
    }
}
