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
    val notifications: List<Notification> = emptyList()
)

class NotificationsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        // Mock data
        _uiState.update { 
            it.copy(
                notifications = listOf(
                    Notification(
                        id = "1",
                        title = "New Event Invitation",
                        message = "You have been invited to 'Beach Cleanup' event.",
                        type = NotificationType.EVENT_INVITATION,
                        timestamp = System.currentTimeMillis() - 3600000
                    ),
                    Notification(
                        id = "2",
                        title = "Event Reminder",
                        message = "Don't forget the 'Weekly Board Games' meetup tomorrow!",
                        type = NotificationType.EVENT_REMINDER,
                        timestamp = System.currentTimeMillis() - 86400000
                    )
                )
            )
        }
    }
}
