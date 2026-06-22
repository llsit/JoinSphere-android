package com.llsit.joinsphere.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileDto(
    @SerialName("id")
    val id: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("email")
    val email: String = "",
    @SerialName("location")
    val location: String = "",
    @SerialName("bio")
    val bio: String = "",
    @SerialName("avatar_url")
    val avatarUrl: String = "https://your-default-avatar-url.com/avatar.png",
    @SerialName("is_verified")
    val isVerified: Boolean = false,
    @SerialName("created_at")
    val createdAt: Long = System.currentTimeMillis(),
    @SerialName("stats")
    val stats: UserStats = UserStats()
)

@Serializable
data class UserStats(
    @SerialName("attendedCount")
    val attendedCount: Int = 0,
    @SerialName("hostedCount")
    val hostedCount: Int = 0,
    @SerialName("rating")
    val rating: Double = 0.0
)
