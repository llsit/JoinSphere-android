package com.llsit.joinsphere.core.domain.repository

import com.llsit.joinsphere.core.model.EventDto

interface EventRepository {
    suspend fun createEvent(event: EventDto): Result<Unit>
    suspend fun uploadCoverImage(imageByteArray: ByteArray): Result<String>
}