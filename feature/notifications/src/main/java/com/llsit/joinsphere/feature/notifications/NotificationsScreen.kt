package com.llsit.joinsphere.feature.notifications

import androidx.compose.foundation.BorderStroke
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Notifications", 
                        fontSize = 24.sp, 
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF004AC6)
                    ) 
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
                    TextButton(onClick = { viewModel.markAllAsRead() }) {
                        Text(
                            "Mark all as read",
                            color = Color(0xFF004AC6),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF9F9F9)
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF9F9F9))
        ) {
            // Tab Navigation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                TabItem("All", uiState.selectedTab == "all") { viewModel.onTabSelected("all") }
                TabItem("Events", uiState.selectedTab == "events") { viewModel.onTabSelected("events") }
                TabItem("Social", uiState.selectedTab == "social") { viewModel.onTabSelected("social") }
            }

            HorizontalDivider(color = Color(0xFFE8E8E8), thickness = 1.dp)

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val now = System.currentTimeMillis()
                val (recent, earlier) = uiState.notifications.partition { 
                    (now - it.timestamp) < 86400000 
                }

                if (recent.isNotEmpty()) {
                    item {
                        SectionHeader("New")
                    }
                    items(recent) { notification ->
                        NotificationCard(notification)
                    }
                }

                if (earlier.isNotEmpty()) {
                    item {
                        SectionHeader("Earlier")
                    }
                    items(earlier) { notification ->
                        NotificationCard(notification)
                    }
                }
            }
        }
    }
}

@Composable
fun TabItem(label: String, selected: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .height(48.dp)
            .clickable(onClick = onClick),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (selected) Color(0xFF004AC6) else Color(0xFF434655)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .clip(RoundedCornerShape(topStart = 100.dp, topEnd = 100.dp))
                .background(if (selected) Color(0xFF004AC6) else Color.Transparent)
        )
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title.uppercase(Locale.ROOT),
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF434655),
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
    )
}

@Composable
fun NotificationCard(notification: Notification) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Box {
            if (!notification.isRead) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .fillMaxHeight()
                        .background(Color(0xFF004AC6))
                )
            }
            
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Icon or Image
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(if (notification.type == NotificationType.FRIEND_REQUEST) CircleShape else RoundedCornerShape(12.dp))
                        .background(Color(0xFFD5E3FC)),
                    contentAlignment = Alignment.Center
                ) {
                    if (notification.imageUrl != null) {
                        AsyncImage(
                            model = notification.imageUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        val icon = when (notification.type) {
                            NotificationType.MESSAGE -> Icons.Default.ChatBubble
                            NotificationType.SYSTEM -> Icons.Default.Info
                            else -> Icons.Default.Notifications
                        }
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = Color(0xFF004AC6),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = notification.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1C1C)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = formatTime(notification.timestamp),
                                fontSize = 11.sp,
                                color = Color(0xFF434655)
                            )
                            if (!notification.isRead) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(Color(0xFF004AC6), CircleShape)
                                )
                            }
                        }
                    }

                    Text(
                        text = notification.message,
                        fontSize = 14.sp,
                        color = Color(0xFF434655),
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    if (notification.actionText != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { },
                            shape = RoundedCornerShape(100.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004AC6)),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text(notification.actionText.toString(), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    if (notification.type == NotificationType.FRIEND_REQUEST) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { /* Accept */ },
                                shape = RoundedCornerShape(100.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD5E3FC), contentColor = Color(0xFF1A1C1C)),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Text("Accept", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                            OutlinedButton(
                                onClick = { /* Ignore */ },
                                shape = RoundedCornerShape(100.dp),
                                border = BorderStroke(1.dp, Color(0xFFC3C6D7)),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Text("Ignore", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF434655))
                            }
                        }
                    }

                    notification.progress?.let { progressValue ->
                        Spacer(modifier = Modifier.height(12.dp))
                        LinearProgressIndicator(
                            progress = { progressValue / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(CircleShape),
                            color = Color(0xFF004AC6),
                            trackColor = Color(0xFFE8E8E8)
                        )
                    }
                }
            }
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
