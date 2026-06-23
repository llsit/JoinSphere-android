package com.llsit.joinsphere.core.domain.repository

import com.llsit.joinsphere.core.model.DiscoverFeedsResponse
import com.llsit.joinsphere.core.model.event.EventDto

interface EventRepository {
    suspend fun createEvent(event: EventDto): Result<Unit>
    suspend fun uploadCoverImage(
        userId: String,
        eventId: String,
        imageByteArray: ByteArray
    ): Result<String>

    suspend fun getDiscoverFeeds(
        userLat: Double,
        userLng: Double,
        radiusMeters: Double = 20000.0
    ): Result<DiscoverFeedsResponse>
}