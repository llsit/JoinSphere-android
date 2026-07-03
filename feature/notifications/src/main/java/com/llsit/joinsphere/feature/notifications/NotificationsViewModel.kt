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
                        title = "New Event",
                        message = "\"Yoga in the Park\" is happening tomorrow! Don't forget your mat and water bottle.",
                        type = NotificationType.EVENT,
                        timestamp = now - 120000, // 2m ago
                        isRead = false,
                        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBHKQWUGx6NoXY-UTqJDhCB8ltAgECOCE_c6hrcqCeTVIi_Kx89ew0GBdd-3Uh5iPP4o9TRvKmTs4loS6IGf6xBvl_rNzYUPWZr76egDEOTyzDp2AR3kcXxwtwIwLrqjwvzFaVdYbwWPvSUxFIVq2_mGxUkOTzlT3TDP65efNQyTL28gaW-9IW6c1E8w-FfIndGWTW11L8u3UtfxKzbfKPWHjicu9GtoTipDWVmfiIIpE1JRA65I5i6Cw",
                        actionText = "View Event",
                    ),
                    Notification(
                        id = "2",
                        title = "Friend Request",
                        message = "Alex Rivers sent you a friend request.",
                        type = NotificationType.FRIEND_REQUEST,
                        timestamp = now - 3600000, // 1h ago
                        isRead = false,
                        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDVwYzdSXypgIRr7JwQ1y-YWOtgjAjlQnlVnK10btXjDMbIzxjpcH6rmQEeGp_xMoGrjpn3RG9Kt_GmjVG7IYAuBxTYMizxgIZ465EDIQ0vEoQA7hj_1r247MtjRI_ElWHS62zNNz2B3jnBRoEhlLARC3Hm8H5xuQlpBhG4ioO_1wSh6qe4fp5ZnK4DsrdfuF-zpkREsjzd1ry5CfKgBTHjVda3EwV4ZqKdkoBGv2fqe7NbFekLhTTJzA",
                        senderName = "Alex Rivers",
                    ),
                    Notification(
                        id = "3",
                        title = "Message",
                        message = "New message from Sarah in 'Weekend Gardening'.",
                        type = NotificationType.MESSAGE,
                        timestamp = now - 10800000, // 3h ago
                        isRead = true,
                        senderName = "Sarah",
                    ),
                    Notification(
                        id = "4",
                        title = "System",
                        message = "Your profile is 90% complete. Add a bio to reach 100% and unlock more community features.",
                        type = NotificationType.SYSTEM,
                        timestamp = now - 86400000, // Yesterday
                        isRead = true,
                        progress = 90,
                    ),
                    Notification(
                        id = "5",
                        title = "Group Update",
                        message = "Lila posted a new photo in 'Photography Club'. Check it out!",
                        type = NotificationType.MESSAGE,
                        timestamp = now - 172800000, // 2d ago
                        isRead = true,
                        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBS3fWFbJ-ycTCisEAokjsv9lsgO3KBthNzfut5Ds9gM3FyjbdO2yEBK4d74S4oFiGLZRzp-VOgAO7-UTst9hXH6Ay1HCOk7_flxrkdmTeeF872BG3mwoawTjGUDoPynp8ahA8qcAHhmsjQ6ZgGiDrRoW2E-opEvbwv5-rSry8ePJGiVavWnbeAv1ccIiBXaJ9mTcRDP2KvO2XRnrZe--qNYLpr14z4towEbJTiqyX0zRVAbPHYKTmliA",
                    )
                )
            )
        }
    }
}
