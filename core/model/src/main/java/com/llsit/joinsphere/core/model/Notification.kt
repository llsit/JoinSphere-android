package com.llsit.joinsphere.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Notification(
    val id: String,
    val title: String,
    val message: String,
    val type: NotificationType,
    val timestamp: Long,
    val isRead: Boolean = false,
    val imageUrl: String? = null,
    val senderName: String? = null,
    val actionText: String? = null,
    val progress: Int? = null,
    val metadata: Map<String, String> = emptyMap()
)

enum class NotificationType {
    EVENT,
    FRIEND_REQUEST,
    MESSAGE,
    SYSTEM
}
