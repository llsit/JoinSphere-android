package com.llsit.joinsphere.core.data.repository

import com.llsit.joinsphere.core.domain.repository.EventRepository
import com.llsit.joinsphere.core.model.DiscoverFeedsResponse
import com.llsit.joinsphere.core.model.event.EventDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.functions.functions
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import io.ktor.client.call.body
import io.ktor.client.request.parameter

class EventRepositoryImpl(
    private val supabase: SupabaseClient
) : EventRepository {

    override suspend fun createEvent(event: EventDto): Result<Unit> = runCatching {
        supabase.from("events").insert(event)
    }

    override suspend fun uploadCoverImage(
        userId: String,
        eventId: String,
        imageByteArray: ByteArray
    ): Result<String> = runCatching {
        val finalUserId = supabase.auth.currentUserOrNull()?.id ?: userId
        val fileName = "$finalUserId/$eventId.jpg"
        val bucket = supabase.storage["event_covers"]

        bucket.upload(fileName, imageByteArray)
        bucket.publicUrl(fileName)
    }

    override suspend fun getDiscoverFeeds(
        userLat: Double,
        userLng: Double,
        radiusMeters: Double
    ): Result<DiscoverFeedsResponse> = runCatching {
        val response = supabase.functions.invoke("get-discover-feeds") {
            parameter("lat", userLat)
            parameter("lng", userLng)
            parameter("radius", radiusMeters)
        }

        response.body<DiscoverFeedsResponse>()
    }
}
