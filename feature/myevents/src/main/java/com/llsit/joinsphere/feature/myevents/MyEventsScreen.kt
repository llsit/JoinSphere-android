package com.llsit.joinsphere.feature.myevents

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.llsit.joinsphere.core.design.Button
import com.llsit.joinsphere.core.design.ButtonSize
import com.llsit.joinsphere.core.design.ButtonVariant
import com.llsit.joinsphere.core.design.Card
import com.llsit.joinsphere.core.design.Progress
import com.llsit.joinsphere.core.model.event.AttendingEvent
import com.llsit.joinsphere.core.model.event.EventDto
import com.llsit.joinsphere.feature.myevents.state.EventState
import org.koin.androidx.compose.koinViewModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.time.ExperimentalTime

enum class MyEventsTab(val label: String) {
    Upcoming("Upcoming"),
    Hosting("Hosting"),
    Saved("Saved"),
    Past("Past")
}

@OptIn(ExperimentalTime::class)
@Composable
fun MyEventsScreen(
    onEventClick: (String, String?, String?) -> Unit = { _, _, _ -> },
    onCreateEventClick: () -> Unit = {},
    onChatClick: (String) -> Unit = {},
    onNotificationClick: () -> Unit = {},
    viewModel: MyEventsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My Events",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Box(
                    modifier = Modifier
                        .offset(y = (-8).dp)
                        .size(44.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .clickable { onNotificationClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notifications",
                        tint = Color(0xFF0D0F14),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

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
                    if (uiState.isLoading && uiState.upcomingEvents.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    } else if (uiState.upcomingEvents.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No upcoming events",
                                    color = Color(0xFF737880),
                                    fontSize = 14.sp
                                )
                            }
                        }
                    } else {
                        items(uiState.upcomingEvents) { event ->
                            UpcomingEventCard(
                                event = event,
                                onDetailsClick = {
                                    onEventClick(
                                        event.id,
                                        event.title,
                                        event.coverImage
                                    )
                                },
                            ) { onChatClick(event.id) }
                        }
                    }
                }

                MyEventsTab.Hosting -> {
                    item {
                        CreateEventButton(onClick = onCreateEventClick)
                    }
                    if (uiState.isLoading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    } else if (uiState.hostingEvents.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "You are not hosting any events yet",
                                    color = Color(0xFF737880),
                                    fontSize = 14.sp
                                )
                            }
                        }
                    } else {
                        items(uiState.hostingEvents) { event ->
                            HostingEventCard(
                                event = event,
                                onDetailsClick = {
                                    onEventClick(
                                        event.id.toString(),
                                        event.title,
                                        event.coverImageUrl
                                    )
                                }
                            )
                        }
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

                MyEventsTab.Saved -> {
                    item {
                        Text(
                            text = "${MOCK_SAVED.size} saved events",
                            fontSize = 13.sp,
                            color = Color(0xFF737880),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                    items(MOCK_SAVED) { event ->
                        // Using mock data version for Saved tab for now
                        val attendingEvent = remember(event) {
                            AttendingEvent(
                                id = event.id.toString(),
                                title = event.title,
                                coverImage = event.image,
                                categoryId = event.category,
                                hostName = event.hostName,
                                hostAvatar = event.hostAvatar,
                                address = event.location,
                                attendeeCount = event.attendees,
                                maxAttendeeCount = event.maxAttendees,
                                isFree = event.price == "Free",
                                price = if (event.price == "Free") 0.0 else event.price.replace(
                                    "$",
                                    ""
                                ).toDoubleOrNull() ?: 0.0,
                                startTimestamp = kotlinx.datetime.Instant.fromEpochMilliseconds(
                                    java.time.OffsetDateTime.now(
                                        java.time.ZoneOffset.UTC
                                    ).toInstant().toEpochMilli()
                                ),
                                endTimestamp = null,
                                unreadCount = event.unread,
                                eventState = EventState.UPCOMING
                            )
                        }
                        UpcomingEventCard(
                            event = attendingEvent,
                            onDetailsClick = {
                                onEventClick(
                                    event.id.toString(),
                                    event.title,
                                    event.image
                                )
                            },
                        ) { onChatClick(event.id.toString()) }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun UpcomingEventCard(
    event: AttendingEvent,
    onDetailsClick: () -> Unit,
    onChatClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.98f else 1f, label = "scale")

    val dateFormatter =
        remember { DateTimeFormatter.ofPattern("EEE, MMM dd").withZone(ZoneId.systemDefault()) }
    val timeFormatter =
        remember { DateTimeFormatter.ofPattern("h:mm a").withZone(ZoneId.systemDefault()) }

    val dateText = remember(event.startTimestamp) {
        val javaInstant = java.time.Instant.ofEpochMilli(event.startTimestamp.toEpochMilliseconds())
        dateFormatter.format(javaInstant)
    }
    val timeText = remember(event.startTimestamp) {
        val javaInstant = java.time.Instant.ofEpochMilli(event.startTimestamp.toEpochMilliseconds())
        timeFormatter.format(javaInstant)
    }

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
                    model = event.coverImage,
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
                    color = when (event.eventState) {
                        EventState.LIVE -> Color(0xFFDC2626) // Red for live
                        EventState.STARTING_SOON -> Color(0xFFF59E0B) // Amber
                        else -> Color(0xFF16A34A) // Green for upcoming
                    },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .padding(top = 12.dp, end = 12.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Text(
                        text = when (event.eventState) {
                            EventState.LIVE -> "● LIVE"
                            EventState.STARTING_SOON -> "⏳ Starting soon"
                            EventState.ENDED -> "Ended"
                            else -> "✓ Confirmed"
                        },
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
                            text = "$dateText · $timeText",
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
                            text = event.address,
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
                        val maxText = event.maxAttendeeCount?.let { "/$it" } ?: ""
                        Text(
                            text = "${event.attendeeCount}$maxText",
                            fontSize = 13.sp,
                            color = Color(0xFF737880)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onDetailsClick,
                        modifier = Modifier.weight(1f),
                        variant = ButtonVariant.Secondary,
                        size = ButtonSize.Sm
                    ) {
                        Text(
                            text = "View details",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
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
                                Text(
                                    text = "Chat",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            if (event.unreadCount > 0) {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .offset(x = 35.dp, y = (-12).dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = event.unreadCount.toString(),
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
    event: EventDto,
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
                    model = event.coverImageUrl,
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
                            text = event.address,
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
                        text = "${event.attendeeCount ?: 0} attending",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "of ${event.maxAttendees ?: 0} spots",
                        fontSize = 12.sp,
                        color = Color(0xFF737880)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Progress(
                    value = (event.attendeeCount ?: 0).toFloat() / (event.maxAttendees
                        ?: 1).coerceAtLeast(1),
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
data class AttendingEventMock(
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

val MOCK_SAVED = listOf(
    AttendingEventMock(
        id = 10,
        title = "Art Gallery Exhibition",
        category = "Arts",
        date = "Sun, Jul 20",
        time = "10:00 AM",
        location = "Modern Art Museum",
        attendees = 12,
        maxAttendees = 100,
        price = "Free",
        status = "confirmed",
        image = "https://images.unsplash.com/photo-1460661419201-fd4cecdf8a8b?w=400&h=280&fit=crop&auto=format",
        hostAvatar = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=60&h=60&fit=crop&auto=format",
        hostName = "Elena Petrova",
        unread = 0
    )
)

@Preview(showBackground = true)
@Composable
fun MyEventsScreenPreview() {
    MyEventsScreen()
}
