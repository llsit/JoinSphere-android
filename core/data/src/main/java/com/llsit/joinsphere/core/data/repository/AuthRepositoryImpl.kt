package com.llsit.joinsphere.core.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.llsit.joinsphere.core.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await

data class UserProfileDto(
    val name: String,
    val email: String,
    val location: String = "",
    val bio: String = "",
    val avatarUrl: String = "https://your-default-avatar-url.com/avatar.png",
    val isVerified: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val stats: Map<String, Any> = mapOf(
        "attendedCount" to 0,
        "hostedCount" to 0,
        "rating" to 5.0
    )
)

class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {
    override suspend fun login(
        email: String,
        password: String
    ): Result<String> = runCatching {
        auth.signInWithEmailAndPassword(email, password).await()
        val userId = auth.currentUser?.uid
        userId ?: throw Exception("User not found")
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): Result<String> = runCatching {
        auth.createUserWithEmailAndPassword(email, password).await()
        val userId = auth.currentUser?.uid
        userId ?: throw Exception("User not found")

        val initialProfile = UserProfileDto(
            name = name,
            email = email
        )

        firestore.collection("users")
            .document(userId)
            .set(initialProfile)
            .await()

        userId
    }

    override suspend fun logout(): Result<Unit> = runCatching {
        auth.signOut()
    }
}