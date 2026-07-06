package com.llsit.joinsphere.core.domain.repository

import com.llsit.joinsphere.core.model.Category
import com.llsit.joinsphere.core.model.DiscoverFeedsResponse
import com.llsit.joinsphere.core.model.event.EventAttendeeDto
import com.llsit.joinsphere.core.model.event.EventDto
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    suspend fun createEvent(event: EventDto): Result<Unit>
    suspend fun getEventDetail(eventId: String): Result<EventDto>
    suspend fun uploadCoverImage(
        userId: String,
        eventId: String,
        imageByteArray: ByteArray
    ): Result<String>

    suspend fun getDiscoverFeeds(
        userLat: Double,
        userLng: Double,
        radiusMeters: Double = 20000.0,
        categoryId: String? = null
    ): Result<DiscoverFeedsResponse>

    fun getCategories(): Flow<List<Category>>
    suspend fun syncCategories(): Result<Unit>
    suspend fun getHostingEvents(userId: String): Result<List<EventDto>>
    suspend fun joinEvent(eventId: String, userId: String): Result<Unit>
    suspend fun cancelJoinEvent(eventId: String, userId: String): Result<Unit>
    suspend fun isUserAttending(eventId: String, userId: String): Result<Boolean>
}