package com.llsit.joinsphere.core.domain.repository

import com.llsit.joinsphere.core.model.UserProfileDto

interface ProfileRepository {
    suspend fun getUserProfile(userId: String): Result<UserProfileDto>
}