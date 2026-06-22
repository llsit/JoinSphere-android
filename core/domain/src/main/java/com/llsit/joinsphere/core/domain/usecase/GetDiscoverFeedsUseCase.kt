package com.llsit.joinsphere.core.domain.usecase

import com.llsit.joinsphere.core.domain.repository.EventRepository
import com.llsit.joinsphere.core.model.DiscoverFeedsResponse

class GetDiscoverFeedsUseCase(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(
        lat: Double = 13.7563, // Bangkok default
        lng: Double = 100.5018,
        radius: Double = 20000.0
    ): Result<DiscoverFeedsResponse> {
        return eventRepository.getDiscoverFeeds(lat, lng, radius)
    }
}