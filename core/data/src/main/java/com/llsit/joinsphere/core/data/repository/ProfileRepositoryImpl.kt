package com.llsit.joinsphere.core.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.llsit.joinsphere.core.domain.repository.ProfileRepository
import com.llsit.joinsphere.core.model.UserProfileDto
import kotlinx.coroutines.tasks.await

class ProfileRepositoryImpl(private val firestore: FirebaseFirestore) : ProfileRepository {
    override suspend fun getUserProfile(userId: String): Result<UserProfileDto> = runCatching {
        firestore.collection("users")
            .document(userId)
            .get()
            .await()
            .toObject(UserProfileDto::class.java) ?: throw Exception("ไม่พบข้อมูลโปรไฟล์")
    }
}