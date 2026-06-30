package com.llsit.joinsphere.feature.myevents

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.llsit.joinsphere.core.design.Button
import com.llsit.joinsphere.core.design.ButtonSize
import com.llsit.joinsphere.core.design.ButtonVariant
import com.llsit.joinsphere.core.design.Card
import com.llsit.joinsphere.core.design.Progress

enum class MyEventsTab(val label: String) {
    Upcoming("Upcoming"),
    Hosting("Hosting"),
    Past("Past")
}

@Composable
fun MyEventsScreen(
    onEventClick: (String, String?, String?) -> Unit = { _, _, _ -> },
    onCreateEventClick: () -> Unit = {},
    onChatClick: (String) -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(MyEventsTab.Upcoming) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Column(
            modifier = Modifier
                .padding(top = 16.dp, start = 20.dp, end = 20.dp, bottom = 12.dp)
        ) {
            Text(
                text = "My Events",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                MyEventsTab.entries.forEach { tab ->
                    val isSelected = selectedTab == tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) Color.White else Color.Transparent)
                            .clickable { selectedTab = tab }
                            .padding(vertical = 9.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = tab.label,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isSelected) Color(0xFF0D0F14) else Color(0xFF737880)
                            )
                            if (tab == MyEventsTab.Upcoming) {
                                val totalUnread = MOCK_ATTENDING.sumOf { it.unread }
                                if (totalUnread > 0) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primary),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = totalUnread.toString(),
                                            color = Color.White,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Content
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (selectedTab) {
                MyEventsTab.Upcoming -> {
                    items(MOCK_ATTENDING) { event ->
                        UpcomingEventCard(
                            event = event,
                            onDetailsClick = { onEventClick(event.id.toString(), event.title, event.image) },
                            onChatClick = { onChatClick(event.id.toString()) }
                        )
                    }
                }
                MyEventsTab.Hosting -> {
                    item {
                        CreateEventButton(onClick = onCreateEventClick)
                    }
                    items(MOCK_HOSTING) { event ->
                        HostingEventCard(
                            event = event,
                            onDetailsClick = { onEventClick(event.id.toString(), event.title, event.image) }
                        )
                    }
                }
                MyEventsTab.Past -> {
                    item {
                        Text(
                            text = "${MOCK_PAST.size} past events",
                            fontSize = 13.sp,
                            color = Color(0xFF737880),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                    items(MOCK_PAST) { event ->
                        PastEventCard(event = event)
                    }
                }
            }
        }
    }
}

@Composable
fun UpcomingEventCard(
    event: AttendingEvent,
    onDetailsClick: () -> Unit,
    onChatClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.98f else 1f, label = "scale")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(),
                onClick = onDetailsClick
            )
    ) {
        Column {
            // Image
            Box(modifier = Modifier.height(128.dp)) {
                AsyncImage(
                    model = event.image,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color(0x8C0D0F14)),
                                startY = 60f
                            )
                        )
                )
                // Status Badge
                Surface(
                    color = if (event.status == "confirmed") Color(0xE616A34A) else Color(0xE6F59E0B),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .padding(top = 12.dp, end = 12.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Text(
                        text = if (event.status == "confirmed") "✓ Confirmed" else "⏳ Pending",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
                // Title
                Text(
                    text = event.title,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.2).sp,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 14.dp, bottom = 12.dp, end = 14.dp)
                )
            }

            // Details
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(13.dp),
                            tint = Color(0xFF737880)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "${event.date} · ${event.time}",
                            fontSize = 13.sp,
                            color = Color(0xFF737880)
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Place,
                            contentDescription = null,
                            modifier = Modifier.size(13.dp),
                            tint = Color(0xFF737880)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = event.location,
                            fontSize = 13.sp,
                            color = Color(0xFF737880),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = event.hostAvatar,
                            contentDescription = null,
                            modifier = Modifier
                                .size(26.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(7.dp))
                        Text(
                            text = event.hostName,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Group,
                            contentDescription = null,
                            modifier = Modifier.size(13.dp),
                            tint = Color(0xFF737880)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "${event.attendees}/${event.maxAttendees}",
                            fontSize = 13.sp,
                            color = Color(0xFF737880)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = onDetailsClick,
                        modifier = Modifier.weight(1f),
                        variant = ButtonVariant.Secondary,
                        size = ButtonSize.Sm
                    ) {
                        Text(text = "View details", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Button(
                        onClick = onChatClick,
                        modifier = Modifier.wrapContentWidth(),
                        variant = ButtonVariant.Secondary,
                        size = ButtonSize.Sm
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Outlined.ChatBubbleOutline,
                                    contentDescription = null,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Chat", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            }
                            if (event.unread > 0) {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .offset(x = 35.dp, y = (-12).dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = event.unread.toString(),
                                        color = Color.White,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HostingEventCard(
    event: HostingEvent,
    onDetailsClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.98f else 1f, label = "scale")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(),
                onClick = onDetailsClick
            )
    ) {
        Column {
            Box(modifier = Modifier.height(128.dp)) {
                AsyncImage(
                    model = event.image,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color(0x8C0D0F14)),
                                startY = 60f
                            )
                        )
                )
                Row(
                    modifier = Modifier
                        .padding(top = 12.dp, end = 12.dp)
                        .align(Alignment.TopEnd),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = { },
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.95f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            modifier = Modifier.size(14.dp),
                            tint = Color(0xFF0D0F14)
                        )
                    }
                    IconButton(
                        onClick = { },
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.95f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            modifier = Modifier.size(14.dp),
                            tint = Color(0xFFDC2626)
                        )
                    }
                }
                Text(
                    text = event.title,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.2).sp,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 14.dp, bottom = 12.dp, end = 14.dp)
                )
            }

            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(13.dp),
                            tint = Color(0xFF737880)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = event.date,
                            fontSize = 13.sp,
                            color = Color(0xFF737880)
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Place,
                            contentDescription = null,
                            modifier = Modifier.size(13.dp),
                            tint = Color(0xFF737880)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = event.location,
                            fontSize = 13.sp,
                            color = Color(0xFF737880)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${event.attendees} attending",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "of ${event.maxAttendees} spots",
                        fontSize = 12.sp,
                        color = Color(0xFF737880)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Progress(
                    value = event.attendees.toFloat() / event.maxAttendees,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun PastEventCard(event: PastEvent) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(0.85f)
    ) {
        Row(modifier = Modifier.height(88.dp)) {
            AsyncImage(
                model = event.image,
                contentDescription = null,
                modifier = Modifier
                    .width(88.dp)
                    .fillMaxHeight(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(14.dp)
            ) {
                Text(
                    text = event.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = event.date,
                    fontSize = 12.sp,
                    color = Color(0xFF737880),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row {
                        repeat(event.rating) {
                            Text(text = "⭐", fontSize = 13.sp)
                        }
                    }
                    Text(
                        text = "${event.attendees} attended",
                        fontSize = 12.sp,
                        color = Color(0xFF737880)
                    )
                }
            }
        }
    }
}

@Composable
fun CreateEventButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        variant = ButtonVariant.Outline,
        size = ButtonSize.Lg
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Create a new event",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// Mock Data
data class AttendingEvent(
    val id: Int,
    val title: String,
    val category: String,
    val date: String,
    val time: String,
    val location: String,
    val attendees: Int,
    val maxAttendees: Int,
    val price: String,
    val status: String,
    val image: String,
    val hostAvatar: String,
    val hostName: String,
    val unread: Int
)

val MOCK_ATTENDING = listOf(
    AttendingEvent(
        id = 1,
        title = "Golden Gate Morning Run",
        category = "Sports",
        date = "Sat, Jun 14",
        time = "7:00 AM",
        location = "Golden Gate Park",
        attendees = 34,
        maxAttendees = 50,
        price = "Free",
        status = "confirmed",
        image = "https://images.unsplash.com/photo-1552674605-db6ffd4facb5?w=400&h=280&fit=crop&auto=format",
        hostAvatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=60&h=80&fit=crop&auto=format",
        hostName = "Sarah Chen",
        unread = 12
    ),
    AttendingEvent(
        id = 2,
        title = "Rooftop Jazz & Wine Night",
        category = "Music",
        date = "Fri, Jun 13",
        time = "7:30 PM",
        location = "SoMa Rooftop",
        attendees = 82,
        maxAttendees = 100,
        price = "$25",
        status = "confirmed",
        image = "https://images.unsplash.com/photo-1415201364774-f6f0bb35f28f?w=400&h=280&fit=crop&auto=format",
        hostAvatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=60&h=80&fit=crop&auto=format",
        hostName = "Marcus Wong",
        unread = 0
    ),
    AttendingEvent(
        id = 3,
        title = "Sunset Yoga on the Beach",
        category = "Outdoors",
        date = "Wed, Jun 18",
        time = "5:30 PM",
        location = "Ocean Beach",
        attendees = 18,
        maxAttendees = 30,
        price = "$10",
        status = "pending",
        image = "https://images.unsplash.com/photo-1506126613408-eca07ce68773?w=400&h=280&fit=crop&auto=format",
        hostAvatar = "https://images.unsplash.com/photo-1554151228-14d9def656e4?w=60&h=60&fit=crop&auto=format",
        hostName = "Priya Nair",
        unread = 3
    )
)

data class HostingEvent(
    val id: Int,
    val title: String,
    val category: String,
    val date: String,
    val time: String,
    val location: String,
    val attendees: Int,
    val maxAttendees: Int,
    val price: String,
    val image: String,
    val unread: Int
)

val MOCK_HOSTING = listOf(
    HostingEvent(
        id = 4,
        title = "Neighborhood Cleanup & Brunch",
        category = "Social",
        date = "Sun, Jun 22",
        time = "9:00 AM",
        location = "Dolores Park",
        attendees = 18,
        maxAttendees = 30,
        price = "Free",
        image = "https://images.unsplash.com/photo-1488521787991-ed7bbaae773c?w=400&h=280&fit=crop&auto=format",
        unread = 5
    )
)

data class PastEvent(
    val id: Int,
    val title: String,
    val date: String,
    val location: String,
    val attendees: Int,
    val image: String,
    val rating: Int
)

val MOCK_PAST = listOf(
    PastEvent(
        id = 5,
        title = "Tech Founders Networking",
        date = "May 30, 2026",
        location = "Caltrain Area",
        attendees = 45,
        image = "https://images.unsplash.com/photo-1540575467063-178a50c2df87?w=300&h=200&fit=crop&auto=format",
        rating = 5
    ),
    PastEvent(
        id = 6,
        title = "Ferry Building Food Tour",
        date = "May 17, 2026",
        location = "Embarcadero",
        attendees = 22,
        image = "https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=300&h=200&fit=crop&auto=format",
        rating = 4
    )
)

@Preview(showBackground = true)
@Composable
fun MyEventsScreenPreview() {
    MyEventsScreen()
}
