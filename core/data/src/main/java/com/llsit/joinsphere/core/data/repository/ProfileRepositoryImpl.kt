package com.llsit.joinsphere.core.data.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import com.llsit.joinsphere.core.domain.repository.ProfileRepository
import com.llsit.joinsphere.core.model.UserProfileDto

class ProfileRepositoryImpl(
    private val supabase: SupabaseClient
) : ProfileRepository {
    override suspend fun getUserProfile(): Result<UserProfileDto> = runCatching {
        val userId = supabase.auth.currentUserOrNull()?.id
            ?: throw Exception("ไม่พบข้อมูลผู้ใช้")
        
        supabase.postgrest["users"]
            .select {
                filter {
                    eq("id", userId)
                }
            }
            .decodeSingle<UserProfileDto>()
    }

    override suspend fun getUserProfile(userId: String): Result<UserProfileDto> = runCatching {
        supabase.postgrest["users"]
            .select {
                filter {
                    eq("id", userId)
                }
            }
            .decodeSingle<UserProfileDto>()
    }

    override suspend fun updateImageProfile(imageByteArray: ByteArray): Result<String> = runCatching {
        val userId = supabase.auth.currentUserOrNull()?.id
            ?: throw Exception("ไม่พบข้อมูลผู้ใช้")
        val bucket = supabase.storage["profile_images"]
        val fileName = "$userId.jpg"

        bucket.upload(fileName, imageByteArray) {
            upsert = true
        }
        
        val downloadUrl = bucket.publicUrl(fileName)
        
        supabase.postgrest["users"].update({
            UserProfileDto::avatarUrl setTo downloadUrl
        }) {
            filter {
                eq("id", userId)
            }
        }
            
        downloadUrl
    }
}
