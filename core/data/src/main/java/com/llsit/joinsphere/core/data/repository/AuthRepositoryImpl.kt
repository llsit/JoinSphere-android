package com.llsit.joinsphere.core.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.llsit.joinsphere.core.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val auth: FirebaseAuth
) : AuthRepository {
    override suspend fun login(
        email: String,
        password: String
    ): Result<String> = runCatching {
        auth.signInWithEmailAndPassword(email, password).await()
        val userId = auth.currentUser?.uid
        userId ?: throw Exception("User not found")
    }.onFailure {
        Result.failure<Throwable>(it).toString()
    }

    override suspend fun register(
        email: String,
        password: String
    ): Result<Unit> = runCatching {
        auth.createUserWithEmailAndPassword(email, password).await()
        Unit
    }

    override suspend fun logout(): Result<Unit> = runCatching {
        auth.signOut()
    }
}