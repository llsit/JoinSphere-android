package com.llsit.joinsphere.core.model

data class UserProfileDto(
    val name: String = "",
    val email: String = "",
    val location: String = "",
    val bio: String = "",
    val avatarUrl: String = "https://your-default-avatar-url.com/avatar.png",
    val isVerified: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val stats: Map<String, Any> = mapOf(
        "attendedCount" to 0,
        "hostedCount" to 0,
        "rating" to 0
    )
)
