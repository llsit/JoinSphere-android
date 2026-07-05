package com.llsit.joinsphere.feature.notifications

import androidx.lifecycle.ViewModel
import com.llsit.joinsphere.core.model.Notification
import com.llsit.joinsphere.core.model.NotificationType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class NotificationsUiState(
    val isLoading: Boolean = false,
    val notifications: List<Notification> = emptyList(),
    val selectedTab: String = "all",
)

class NotificationsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    init {
        loadNotifications()
    }

    fun onTabSelected(tab: String) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun markAllAsRead() {
        _uiState.update { state ->
            state.copy(notifications = state.notifications.map { it.copy(isRead = true) })
        }
    }

    private fun loadNotifications() {
        val now = System.currentTimeMillis()
        _uiState.update {
            it.copy(
                notifications = listOf(
                    Notification(
                        id = "1",
                        title = "RSVP Confirmed",
                        message = "You're confirmed for Golden Gate Morning Run this Saturday.",
                        type = NotificationType.EVENT,
                        timestamp = now - 600000, // 10m ago
                        isRead = false,
                        imageUrl = null,
                        actionText = null
                    ),
                    Notification(
                        id = "2",
                        title = "New Participant",
                        message = "Priya Nair just joined your pottery workshop.",
                        type = NotificationType.FRIEND_REQUEST,
                        timestamp = now - 3600000, // 1h ago
                        isRead = false,
                        senderName = "Priya Nair"
                    ),
                    Notification(
                        id = "3",
                        title = "New message in Golden Gate Run",
                        message = "Sarah: See everyone Saturday! Don't forget water 💧",
                        type = NotificationType.MESSAGE,
                        timestamp = now - 7200000, // 2h ago
                        isRead = false,
                        senderName = "Sarah"
                    ),
                    Notification(
                        id = "4",
                        title = "Event Reminder",
                        message = "Rooftop Jazz & Wine is tomorrow at 7:30 PM.",
                        type = NotificationType.EVENT,
                        timestamp = now - 86400000, // Yesterday
                        isRead = true
                    ),
                    Notification(
                        id = "5",
                        title = "Activity Updated",
                        message = "Golden Gate Run updated the meeting point.",
                        type = NotificationType.EVENT,
                        timestamp = now - 100000000, // Yesterday
                        isRead = true
                    ),
                    Notification(
                        id = "6",
                        title = "Friend Joined",
                        message = "Marcus Wong also joined Pottery Workshop.",
                        type = NotificationType.FRIEND_REQUEST,
                        timestamp = now - 120000000, // Yesterday
                        isRead = true
                    ),
                    Notification(
                        id = "7",
                        title = "Review Reminder",
                        message = "How was Farmers Market last Sunday? Leave a review!",
                        type = NotificationType.SYSTEM,
                        timestamp = now - 140000000, // Yesterday
                        isRead = true
                    ),
                    Notification(
                        id = "8",
                        title = "Activity Cancelled",
                        message = "Beach Volleyball was cancelled due to weather.",
                        type = NotificationType.EVENT,
                        timestamp = now - 200000000, // Earlier
                        isRead = true
                    ),
                    Notification(
                        id = "9",
                        title = "Safety Alert",
                        message = "Verify your account to unlock all features.",
                        type = NotificationType.SYSTEM,
                        timestamp = now - 300000000, // Earlier
                        isRead = true
                    )
                )
            )
        }
    }
}
