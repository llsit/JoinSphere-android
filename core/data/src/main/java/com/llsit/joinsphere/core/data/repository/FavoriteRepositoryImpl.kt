package com.llsit.joinsphere.core.data.repository

import com.llsit.joinsphere.core.domain.repository.FavoriteRepository
import com.llsit.joinsphere.core.model.event.FavoriteDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update

class FavoriteRepositoryImpl(
    private val supabase: SupabaseClient
) : FavoriteRepository {

    private val _favorites = MutableStateFlow<List<String>>(emptyList())
    private var isInitialized = false

    override suspend fun addFavorite(eventId: String): Result<Unit> = runCatching {
        val userId = supabase.auth.currentUserOrNull()?.id ?: throw Exception("User not logged in")

        supabase.from("favorite_events").insert(
            mapOf(
                "user_id" to userId,
                "event_id" to eventId
            )
        )
        _favorites.update { it + eventId }
    }

    override suspend fun removeFavorite(eventId: String): Result<Unit> = runCatching {
        val userId = supabase.auth.currentUserOrNull()?.id ?: throw Exception("User not logged in")

        supabase.from("favorite_events")
            .delete {
                filter {
                    eq("user_id", userId)
                    eq("event_id", eventId)
                }
            }
        _favorites.update { it - eventId }
    }

    override fun observeFavorites(): Flow<List<String>> = _favorites.onStart {
        if (!isInitialized) {
            refreshFavorites()
            isInitialized = true
        }
    }

    private suspend fun refreshFavorites() {
        val userId = supabase.auth.currentUserOrNull()?.id ?: return
        runCatching {
            val result = supabase
                .from("favorite_events")
                .select {
                    filter {
                        eq("user_id", userId)
                    }
                }
                .decodeList<FavoriteDto>()
            _favorites.value = result.map { it.eventId }
        }
    }
}
