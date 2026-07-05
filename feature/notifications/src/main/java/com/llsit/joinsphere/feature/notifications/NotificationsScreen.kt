package com.llsit.joinsphere.feature.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.llsit.joinsphere.core.model.Notification
import com.llsit.joinsphere.core.model.NotificationType
import org.koin.androidx.compose.koinViewModel

import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    onBackClick: () -> Unit = {},
    viewModel: NotificationsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val totalUnread = remember(uiState.notifications) { 
        uiState.notifications.count { !it.isRead } 
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "Notifications", 
                            fontSize = 24.sp, 
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF0F172A), // SLATE_900 equivalent
                            letterSpacing = (-0.7).sp
                        )
                        if (totalUnread > 0) {
                            Spacer(modifier = Modifier.width(10.dp))
                            Surface(
                                color = Color(0xFF2563EB), // BLUE
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = totalUnread.toString(),
                                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 3.dp),
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, 
                            contentDescription = "Back",
                            tint = Color(0xFF1A1C1C)
                        )
                    }
                },
                actions = {
                    if (totalUnread > 0) {
                        TextButton(onClick = { viewModel.markAllAsRead() }) {
                            Text(
                                "Mark all read",
                                color = Color(0xFF2563EB),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
        ) {
            val now = System.currentTimeMillis()
            val today = uiState.notifications.filter { (now - it.timestamp) < 86400000 }
            val yesterday = uiState.notifications.filter { 
                val diff = now - it.timestamp
                diff in 86400000..172800000 
            }
            val earlier = uiState.notifications.filter { (now - it.timestamp) > 172800000 }

            if (uiState.notifications.isEmpty()) {
                EmptyNotifications()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (today.isNotEmpty()) {
                        item { SectionHeader("Today") }
                        items(today) { notification ->
                            NotificationRow(notification)
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 20.dp),
                                color = Color(0xFFF1F5F9), // SLATE_100
                                thickness = 1.dp
                            )
                        }
                    }

                    if (yesterday.isNotEmpty()) {
                        item { SectionHeader("Yesterday") }
                        items(yesterday) { notification ->
                            NotificationRow(notification)
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 20.dp),
                                color = Color(0xFFF1F5F9),
                                thickness = 1.dp
                            )
                        }
                    }

                    if (earlier.isNotEmpty()) {
                        item { SectionHeader("Earlier") }
                        items(earlier) { notification ->
                            NotificationRow(notification)
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 20.dp),
                                color = Color(0xFFF1F5F9),
                                thickness = 1.dp
                            )
                        }
                    }
                    
                    item { Spacer(modifier = Modifier.height(32.dp)) }
                }
            }
        }
    }
}

@Composable
fun EmptyNotifications() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🔔", fontSize = 52.sp)
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            "You're all caught up!",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            "Notifications about your events, chats, and connections will appear here.",
            fontSize = 14.sp,
            color = Color(0xFF64748B), // SLATE_500
            lineHeight = 22.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title.uppercase(Locale.ROOT),
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF94A3B8), // SLATE_400
        modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 8.dp),
        letterSpacing = 0.6.sp
    )
}

@Composable
fun NotificationRow(notification: Notification) {
    val iconBg = when (notification.type) {
        NotificationType.EVENT -> Color(0xFFD1FAE5) // Green
        NotificationType.FRIEND_REQUEST -> Color(0xFFDBEAFE) // Blue
        NotificationType.MESSAGE -> Color(0xFFEDE9FE) // Purple
        NotificationType.SYSTEM -> Color(0xFFFEF3C7) // Amber
    }
    
    val iconEmoji = when (notification.type) {
        NotificationType.EVENT -> "✅"
        NotificationType.FRIEND_REQUEST -> "👤"
        NotificationType.MESSAGE -> "💬"
        NotificationType.SYSTEM -> "🛡️"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Navigate */ }
            .padding(vertical = 14.dp, horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Icon Circle
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Text(text = iconEmoji, fontSize = 20.sp)
        }

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = notification.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    modifier = Modifier.weight(1f)
                )
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = formatTime(notification.timestamp),
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8)
                    )
                    if (!notification.isRead) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color(0xFF2563EB), CircleShape)
                        )
                    }
                }
            }

            Text(
                text = notification.message,
                fontSize = 14.sp,
                color = Color(0xFF64748B),
                lineHeight = 20.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

fun formatTime(timestamp: Long): String {
    val diff = System.currentTimeMillis() - timestamp
    return when {
        diff < 60000 -> "Just now"
        diff < 3600000 -> "${diff / 60000}m ago"
        diff < 86400000 -> "${diff / 3600000}h ago"
        diff < 172800000 -> "Yesterday"
        else -> "${diff / 86400000}d ago"
    }
}
