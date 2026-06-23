package com.llsit.joinsphere.core.domain.usecase

import com.llsit.joinsphere.core.domain.repository.EventRepository
import com.llsit.joinsphere.core.model.DiscoverFeedsResponse

class GetDiscoverFeedsUseCase(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(
        lat: Double,
        lng: Double,
        radius: Double
    ): Result<DiscoverFeedsResponse> {
        return eventRepository.getDiscoverFeeds(lat, lng, radius)
    }
}