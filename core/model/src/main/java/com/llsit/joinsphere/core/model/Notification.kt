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
    val metadata: Map<String, String> = emptyMap()
)

enum class NotificationType {
    EVENT_INVITATION,
    EVENT_REMINDER,
    CHAT_MESSAGE,
    SYSTEM
}
