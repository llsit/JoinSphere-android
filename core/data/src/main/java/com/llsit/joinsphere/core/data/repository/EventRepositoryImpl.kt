package com.llsit.joinsphere.core.data.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import com.llsit.joinsphere.core.domain.repository.EventRepository
import com.llsit.joinsphere.core.model.EventDto
import java.util.UUID

class EventRepositoryImpl(
    private val supabase: SupabaseClient
) : EventRepository {

    override suspend fun createEvent(event: EventDto): Result<Unit> = runCatching {
        supabase.postgrest["events"].insert(event)
    }

    override suspend fun uploadCoverImage(imageByteArray: ByteArray): Result<String> = runCatching {
        val fileName = "${UUID.randomUUID()}.jpg"
        val bucket = supabase.storage["event_covers"]
        
        bucket.upload(fileName, imageByteArray)
        bucket.publicUrl(fileName)
    }
}
