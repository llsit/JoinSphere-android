package com.llsit.joinsphere.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    suspend fun addFavorite(eventId: String): Result<Unit>
    suspend fun removeFavorite(eventId: String): Result<Unit>
    fun observeFavorites(): Flow<List<String>>
}
