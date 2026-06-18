package com.llsit.joinsphere.core.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.llsit.joinsphere.core.data.local.PreferencesDataSource
import com.llsit.joinsphere.core.domain.repository.ProfileRepository
import com.llsit.joinsphere.core.model.UserProfileDto
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

class ProfileRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val pref: PreferencesDataSource
) : ProfileRepository {
    override suspend fun getUserProfile(): Result<UserProfileDto> = runCatching {
        firestore.collection("users")
            .document(pref.authToken.first() ?: "")
            .get()
            .await()
            .toObject(UserProfileDto::class.java) ?: throw Exception("ไม่พบข้อมูลโปรไฟล์")
    }

    override suspend fun updateImageProfile(imageByteArray: ByteArray): Result<String> = runCatching {
        val userId = pref.authToken.first() ?: throw Exception("ไม่พบข้อมูลผู้ใช้")
        val storageRef = storage.reference.child("profile_images/$userId.jpg")

        storageRef.putBytes(imageByteArray).await()
        val downloadUrl = storageRef.downloadUrl.await().toString()
        firestore.collection("users")
            .document(userId)
            .update("avatarUrl", downloadUrl)
            .await()
            
        downloadUrl
    }
}