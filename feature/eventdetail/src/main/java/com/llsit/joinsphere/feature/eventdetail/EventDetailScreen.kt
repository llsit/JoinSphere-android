package com.llsit.joinsphere.feature.eventdetail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel

@Composable
fun EventDetailScreen(
    eventId: String,
    initialTitle: String? = null,
    initialImage: String? = null,
    viewModel: EventDetailViewModel = koinViewModel(),
    onBackClick: () -> Unit = {},
    onJoinClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(eventId) {
        viewModel.loadEventDetail(eventId)
    }

    var isLiked by remember { mutableStateOf(false) }

    val event = uiState.event
    val host = uiState.host

    // Use initial data if real data is still loading
    val displayTitle = event?.title ?: initialTitle ?: ""
    val displayImage = event?.coverImageUrl ?: initialImage

    val categoryLabel = remember(event, uiState.categories) {
        uiState.categories.find { it.id == event?.categoryId }?.label ?: "Event"
    }
    val categoryEmoji = remember(event, uiState.categories) {
        uiState.categories.find { it.id == event?.categoryId }?.emoji ?: "📅"
    }

    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val shimmerAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Main Content Layer
        if (event != null || initialTitle != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 100.dp)
            ) {
                // Header Image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    AsyncImage(
                        model = displayImage
                            ?: "https://images.unsplash.com/photo-1552674605-db6ffd4facb5?w=800&h=600&fit=crop&auto=format",
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    // Gradient Overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.4f),
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.6f)
                                    ),
                                    startY = 0f
                                )
                            )
                    )

                    // Top Bar Actions
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 48.dp, start = 20.dp, end = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier.background(
                                Color.White.copy(alpha = 0.2f),
                                CircleShape
                            )
                        ) {
                            Icon(
                                Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            IconButton(
                                onClick = { /* Share */ },
                                modifier = Modifier.background(
                                    Color.White.copy(alpha = 0.2f),
                                    CircleShape
                                )
                            ) {
                                Icon(
                                    Icons.Outlined.Share,
                                    contentDescription = "Share",
                                    tint = Color.White
                                )
                            }
                            IconButton(
                                onClick = { isLiked = !isLiked },
                                modifier = Modifier.background(
                                    Color.White.copy(alpha = 0.2f),
                                    CircleShape
                                )
                            ) {
                                Icon(
                                    imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                    contentDescription = "Like",
                                    tint = if (isLiked) Color.Red else Color.White
                                )
                            }
                        }
                    }

                    // Solo Badge on Image
                    if (event?.isSoloFriendly == true) {
                        Surface(
                            color = Color.White.copy(alpha = 0.9f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .padding(20.dp)
                                .align(Alignment.BottomStart)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Color(0xFF1757F0),
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    "Solo-friendly",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1757F0)
                                )
                            }
                        }
                    }
                }

                Column(modifier = Modifier.padding(24.dp)) {
                    // Category & Rating
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (event != null) {
                            Surface(
                                color = Color(0xFFE5EDFF),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "$categoryEmoji $categoryLabel",
                                    modifier = Modifier.padding(
                                        horizontal = 10.dp,
                                        vertical = 4.dp
                                    ),
                                    color = Color(0xFF1757F0),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFF59E0B),
                                modifier = Modifier.size(16.dp)
                            )
                            Text("4.9", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("(127 reviews)", color = Color.Gray, fontSize = 14.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Title
                    Text(
                        text = displayTitle,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 34.sp,
                        letterSpacing = (-0.5).sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    AnimatedContent(
                        targetState = event,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(500)) togetherWith
                                    fadeOut(animationSpec = tween(500))
                        },
                        label = "eventContent"
                    ) { targetEvent ->
                        if (targetEvent != null) {
                            Column {
                                // Date & Time
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        color = Color(0xFFF3F4F6),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.size(44.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                Icons.Default.Schedule,
                                                contentDescription = null,
                                                tint = Color(0xFF737880)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column {
                                        Text(
                                            targetEvent.date,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 15.sp
                                        )
                                        Text(targetEvent.time, color = Color.Gray, fontSize = 13.sp)
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                // Location
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        color = Color(0xFFF3F4F6),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.size(44.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                Icons.Default.Place,
                                                contentDescription = null,
                                                tint = Color(0xFF737880)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column {
                                        Text(
                                            targetEvent.address,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 15.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(32.dp))

                                // Host Section
                                Text("Hosted by", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(12.dp))
                                Surface(
                                    color = Color(0xFFF9FAFB),
                                    shape = RoundedCornerShape(20.dp),
                                    border = BorderStroke(1.dp, Color(0xFFF3F4F6))
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        AsyncImage(
                                            model = host?.avatarUrl
                                                ?: "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=120&h=120&fit=crop&auto=format",
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(48.dp)
                                                .clip(CircleShape),
                                            contentScale = ContentScale.Crop
                                        )
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Text(
                                                    host?.name ?: "Organizer",
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 15.sp
                                                )
                                                Icon(
                                                    Icons.Default.Shield,
                                                    contentDescription = "Verified",
                                                    tint = Color(0xFF1757F0),
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }
                                            Text("Host", color = Color.Gray, fontSize = 13.sp)
                                        }
                                        OutlinedButton(
                                            onClick = { /* Message host */ },
                                            shape = RoundedCornerShape(12.dp),
                                            border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                                        ) {
                                            Text("Contact", fontSize = 13.sp, color = Color.Black)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(32.dp))

                                // Description
                                Text(
                                    "About this event",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = targetEvent.description ?: "No description provided.",
                                    fontSize = 15.sp,
                                    lineHeight = 24.sp,
                                    color = Color(0xFF4B5563)
                                )

                                Spacer(modifier = Modifier.height(32.dp))

                                // Attendees
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "Attendees (${targetEvent.attendeeCount ?: 0})",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        } else {
                            // Loading placeholders for event details
                            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(44.dp)
                                        .alpha(shimmerAlpha)
                                        .background(Color(0xFFF3F4F6), RoundedCornerShape(12.dp))
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(44.dp)
                                        .alpha(shimmerAlpha)
                                        .background(Color(0xFFF3F4F6), RoundedCornerShape(12.dp))
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp)
                                        .alpha(shimmerAlpha)
                                        .background(Color(0xFFF3F4F6), RoundedCornerShape(12.dp))
                                )
                            }
                        }
                    }
                }
            }

            // Sticky Bottom Bar
            Surface(
                modifier = Modifier.align(Alignment.BottomCenter),
                shadowElevation = 16.dp,
                color = Color.White
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Price", color = Color.Gray, fontSize = 12.sp)
                        Crossfade(targetState = event, label = "priceCrossfade") { targetEvent ->
                            Text(
                                if (targetEvent?.isFree == true) "Free" else if (targetEvent != null) "${targetEvent.price} THB" else "...",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp,
                                color = Color(0xFF16A34A)
                            )
                        }
                    }
                    Button(
                        onClick = onJoinClick,
                        enabled = event != null,
                        modifier = Modifier
                            .height(56.dp)
                            .weight(1f)
                            .padding(start = 24.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1757F0))
                    ) {
                        Text("Join this event", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else if (uiState.isLoading) {
            androidx.compose.material3.CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (uiState.error != null) {
            Text(
                text = uiState.error!!,
                modifier = Modifier.align(Alignment.Center),
                color = Color.Red
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventDetailScreenPreview() {
    EventDetailScreen(eventId = "preview_id")
}
