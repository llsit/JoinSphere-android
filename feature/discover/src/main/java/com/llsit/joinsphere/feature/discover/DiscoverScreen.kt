package com.llsit.joinsphere.feature.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.llsit.joinsphere.core.design.Card

data class Category(val id: String, val label: String)
data class Event(
    val id: Int,
    val title: String,
    val category: String,
    val image: String,
    val date: String,
    val time: String,
    val location: String,
    val attendees: Int,
    val maxAttendees: Int? = null,
    val rating: Float,
    val reviews: Int? = null,
    val price: String,
    val soloPercent: Int,
    val hostVerified: Boolean,
    val hostName: String? = null,
    val hostAvatar: String? = null,
    val avatars: List<String> = emptyList()
)

val CATEGORIES = listOf(
    Category("all", "All"),
    Category("sports", "Sports"),
    Category("music", "Music"),
    Category("food", "Food & Drink"),
    Category("arts", "Arts"),
    Category("outdoors", "Outdoors"),
    Category("tech", "Tech")
)

val FEATURED = listOf(
    Event(
        id = 1,
        title = "Golden Gate Morning Run",
        category = "Sports",
        image = "https://images.unsplash.com/photo-1552674605-db6ffd4facb5?w=700&h=460&fit=crop&auto=format",
        date = "Sat, Jun 14",
        time = "7:00 AM",
        location = "Golden Gate Park",
        attendees = 34,
        maxAttendees = 50,
        rating = 4.9f,
        reviews = 127,
        price = "Free",
        soloPercent = 71,
        hostVerified = true,
        hostName = "Sarah C.",
        hostAvatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=80&h=80&fit=crop&auto=format",
        avatars = listOf(
            "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=60&h=60&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1527980965255-d3b416303d12?w=60&h=60&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=60&h=60&fit=crop&auto=format"
        )
    ),
    Event(
        id = 2,
        title = "Rooftop Jazz & Wine Night",
        category = "Music",
        image = "https://images.unsplash.com/photo-1415201364774-f6f0bb35f28f?w=700&h=460&fit=crop&auto=format",
        date = "Fri, Jun 13",
        time = "7:30 PM",
        location = "SoMa Rooftop",
        attendees = 82,
        maxAttendees = 100,
        rating = 4.8f,
        reviews = 94,
        price = "$25",
        soloPercent = 58,
        hostVerified = true,
        hostName = "Marcus W.",
        hostAvatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=60&h=60&fit=crop&auto=format",
        avatars = listOf(
            "https://images.unsplash.com/photo-1554151228-14d9def656e4?w=60&h=60&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=60&h=60&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=60&h=60&fit=crop&auto=format"
        )
    )
)

val NEARBY = listOf(
    Event(
        id = 3,
        title = "Sunday Farmers Market",
        category = "Food & Drink",
        date = "Sun, Jun 15",
        time = "9:00 AM",
        location = "Ferry Building",
        attendees = 150,
        price = "Free",
        soloPercent = 82,
        rating = 4.7f,
        image = "https://images.unsplash.com/photo-1488459716781-31db52582fe9?w=400&h=300&fit=crop&auto=format",
        hostVerified = true
    ),
    Event(
        id = 4,
        title = "Pottery Workshop",
        category = "Arts",
        date = "Thu, Jun 19",
        time = "2:00 PM",
        location = "Mission Arts Center",
        attendees = 12,
        price = "$45",
        soloPercent = 90,
        rating = 5.0f,
        image = "https://images.unsplash.com/photo-1565193566173-7a0ee3dbe261?w=400&h=300&fit=crop&auto=format",
        hostVerified = true
    ),
    Event(
        id = 5,
        title = "Sunset Yoga on the Beach",
        category = "Outdoors",
        date = "Wed, Jun 18",
        time = "5:30 PM",
        location = "Ocean Beach",
        attendees = 28,
        price = "$10",
        soloPercent = 76,
        rating = 4.9f,
        image = "https://images.unsplash.com/photo-1506126613408-eca07ce68773?w=400&h=300&fit=crop&auto=format",
        hostVerified = false
    )
)

@Composable
fun DiscoverScreen(
    onEventClick: (Int) -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onSearchClick: () -> Unit = {}
) {
    var activeCategory by remember { mutableStateOf("all") }
    val liked = remember { mutableStateListOf<Int>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Column(
            modifier = Modifier.padding(
                start = 20.dp,
                end = 20.dp,
                top = 16.dp,
                bottom = 12.dp
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "San Francisco, CA",
                        color = Color(0xFF737880),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                    Text(
                        text = "Discover",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.5).sp,
                        lineHeight = 28.sp,
                        modifier = Modifier.clickable { onSearchClick() }
                    )
                }

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFF3F4F6))
                        .clickable { onNotificationClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notifications",
                        tint = Color(0xFF0D0F14),
                        modifier = Modifier.size(20.dp)
                    )
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .align(Alignment.TopEnd)
                            .offset(x = (-10).dp, y = 10.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1757F0))
                            .border(2.dp, Color.White, CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Trust banner
            Surface(
                color = Color(0xFFE5EDFF),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = Color(0xFF1757F0),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "All hosts are ID-verified and reviewed by our community",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1757F0)
                    )
                }
            }
        }

        // Category chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            items(CATEGORIES) { cat ->
                val active = activeCategory == cat.id
                Surface(
                    onClick = { activeCategory = cat.id },
                    color = if (active) Color(0xFF0D0F14) else Color(0xFFF4F5F8),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = cat.label,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (active) Color.White else Color(0xFF737880)
                    )
                }
            }
        }

        // Featured events section
        Column(modifier = Modifier.padding(bottom = 24.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Trending near you",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.3).sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.clickable { }
                ) {
                    Text(
                        text = "See all",
                        color = Color(0xFF1757F0),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = Color(0xFF1757F0),
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                items(FEATURED) { ev ->
                    FeaturedEventCard(
                        event = ev,
                        isLiked = liked.contains(ev.id),
                        onLikeToggle = {
                            if (liked.contains(ev.id)) liked.remove(ev.id) else liked.add(ev.id)
                        },
                        onClick = { onEventClick(ev.id) }
                    )
                }
            }
        }

        // Nearby this week section
        Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 24.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "This week",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.3).sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.clickable { }
                ) {
                    Text(
                        text = "See all",
                        color = Color(0xFF1757F0),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = Color(0xFF1757F0),
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                NEARBY.forEach { ev ->
                    NearbyEventCard(
                        event = ev,
                        onClick = { onEventClick(ev.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun FeaturedEventCard(
    event: Event,
    isLiked: Boolean,
    onLikeToggle: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(272.dp)
            .clickable { onClick() }
    ) {
        Column {
            Box(modifier = Modifier.height(160.dp)) {
                AsyncImage(
                    model = event.image,
                    contentDescription = event.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color(0x800D0F14)),
                                startY = 80f
                            )
                        )
                )

                // Like Button
                Surface(
                    onClick = onLikeToggle,
                    shape = CircleShape,
                    color = Color.White,
                    modifier = Modifier
                        .padding(12.dp)
                        .size(36.dp)
                        .align(Alignment.TopEnd),
                    shadowElevation = 2.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (isLiked) Color(0xFFDC2626) else Color(0xFF737880),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // Solo Badge
                Surface(
                    color = Color.White.copy(alpha = 0.95f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.BottomStart)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color(0xFF1757F0),
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "${event.soloPercent}% came solo",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1757F0)
                        )
                    }
                }

                // Price
                Surface(
                    color = Color.White.copy(alpha = 0.95f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.BottomEnd)
                ) {
                    Text(
                        text = event.price,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF0D0F14)
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = event.category,
                        color = Color(0xFF1757F0),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFF59E0B),
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = event.rating.toString(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (event.reviews != null) {
                            Text(
                                text = "(${event.reviews})",
                                color = Color(0xFF737880),
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = event.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.2).sp,
                    lineHeight = 19.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = Color(0xFF737880),
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "${event.date} · ${event.time}",
                        color = Color(0xFF737880),
                        fontSize = 12.sp
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = null,
                        tint = Color(0xFF737880),
                        modifier = Modifier.size(12.dp)
                    )
                    Text(text = event.location, color = Color(0xFF737880), fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AvatarStack(avatars = event.avatars, size = 24)
                        Text(
                            text = "${event.attendees} going",
                            color = Color(0xFF737880),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    if (event.hostVerified && event.hostAvatar != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            AsyncImage(
                                model = event.hostAvatar,
                                contentDescription = event.hostName,
                                modifier = Modifier
                                    .size(18.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = Color(0xFF1757F0),
                                modifier = Modifier.size(11.dp)
                            )
                            if (event.hostName != null) {
                                Text(
                                    text = event.hostName,
                                    color = Color(0xFF737880),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NearbyEventCard(event: Event, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(modifier = Modifier.height(110.dp)) {
            Box(modifier = Modifier.width(100.dp)) {
                AsyncImage(
                    model = event.image,
                    contentDescription = event.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                if (event.hostVerified) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(22.dp)
                            .align(Alignment.BottomStart),
                        shadowElevation = 2.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = Color(0xFF1757F0),
                                modifier = Modifier.size(11.dp)
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(14.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = event.category,
                        color = Color(0xFF1757F0),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = event.price,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (event.price == "Free") Color(0xFF16A34A) else Color(0xFF0D0F14)
                    )
                }

                Text(
                    text = event.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.2).sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = Color(0xFF737880),
                        modifier = Modifier.size(11.dp)
                    )
                    Text(
                        text = "${event.date} · ${event.time}",
                        color = Color(0xFF737880),
                        fontSize = 12.sp
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color(0xFF1757F0),
                            modifier = Modifier.size(11.dp)
                        )
                        Text(
                            text = "${event.soloPercent}% solo",
                            color = Color(0xFF1757F0),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFF59E0B),
                            modifier = Modifier.size(11.dp)
                        )
                        Text(
                            text = event.rating.toString(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AvatarStack(avatars: List<String>, size: Int = 28) {
    Box(contentAlignment = Alignment.CenterStart) {
        avatars.forEachIndexed { index, src ->
            AsyncImage(
                model = src,
                contentDescription = null,
                modifier = Modifier
                    .offset(x = (index * (size * 0.7f)).dp)
                    .size(size.dp)
                    .zIndex((avatars.size - index).toFloat())
                    .clip(CircleShape)
                    .border(2.dp, Color.White, CircleShape)
                    .background(Color(0xFFF4F5F8)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DiscoverScreenPreview() {
    DiscoverScreen()
}
