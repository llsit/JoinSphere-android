package com.llsit.joinsphere.core.domain.repository

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<String>
    suspend fun register(name: String, email: String, password: String): Result<String>
    suspend fun logout(): Result<Unit>
    suspend fun refreshSession(): Result<Boolean>
    fun getCurrentUserId(): String?
}