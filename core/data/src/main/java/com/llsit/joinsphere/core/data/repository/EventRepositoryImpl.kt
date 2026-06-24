package com.llsit.joinsphere.core.data.repository

import com.llsit.joinsphere.core.database.dao.CategoryDao
import com.llsit.joinsphere.core.database.entity.toEntity
import com.llsit.joinsphere.core.database.entity.toExternalModel
import com.llsit.joinsphere.core.domain.repository.EventRepository
import com.llsit.joinsphere.core.model.Category
import com.llsit.joinsphere.core.model.DiscoverFeedsResponse
import com.llsit.joinsphere.core.model.event.EventDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.functions.functions
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import io.ktor.client.call.body
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import timber.log.Timber

class EventRepositoryImpl(
    private val supabase: SupabaseClient,
    private val categoryDao: CategoryDao
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
        val response = supabase.functions.invoke(
            "get-discover-feeds",
            buildJsonObject {
                put("user_lat", userLat)
                put("user_lng", userLng)
                put("radius_m", radiusMeters)
            }
        )
        response.body<DiscoverFeedsResponse>()
    }.onFailure {
        Timber.e(it, "Error getting discover feeds")
    }

    override fun getCategories(): Flow<List<Category>> {
        return categoryDao.getCategories().map { entities ->
            entities.map { it.toExternalModel() }
        }
    }

    override suspend fun syncCategories(): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val remoteCategories = supabase.from("categories")
                .select()
                .decodeList<Category>()

            categoryDao.deleteAllCategories()
            categoryDao.insertCategories(remoteCategories.map { it.toEntity() })
        }.onFailure {
            Timber.e(it, "Error syncing categories")
        }
    }
}
