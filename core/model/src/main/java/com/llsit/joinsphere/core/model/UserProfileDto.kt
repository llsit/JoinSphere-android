package com.llsit.joinsphere.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileDto(
    @SerialName("id")
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val location: String = "",
    val bio: String = "",
    val avatarUrl: String = "https://your-default-avatar-url.com/avatar.png",
    val isVerified: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val stats: UserStats = UserStats()
)

@Serializable
data class UserStats(
    val attendedCount: Int = 0,
    val hostedCount: Int = 0,
    val rating: Double = 0.0
)
