package com.llsit.joinsphere.feature.profile

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.llsit.joinsphere.core.design.Card
import com.llsit.joinsphere.core.model.UserProfileDto
import com.llsit.joinsphere.feature.profile.state.ProfileIntent
import com.llsit.joinsphere.feature.profile.state.ProfileUiEffect
import org.koin.androidx.compose.koinViewModel
import java.time.Instant
import java.time.ZoneId

data class Stat(val label: String, val value: String)
data class Badge(val emoji: String, val label: String, val bg: Color, val color: Color)

val BADGES = listOf(
    Badge("🏃", "Active Runner", Color(0xFFEFF6FF), Color(0xFF1D4ED8)),
    Badge("🎵", "Music Fan", Color(0xFFF5F3FF), Color(0xFF6D28D9)),
    Badge("⭐", "Top Host", Color(0xFFFFFBEB), Color(0xFFD97706)),
    Badge("🤝", "Connector", Color(0xFFECFDF5), Color(0xFF059669))
)

@Composable
fun ProfileScreen(
    onLogout: () -> Unit = {},
    onEditProfileClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    profileViewModel: ProfileViewModel = koinViewModel()
) {
    val uiState by profileViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            profileViewModel.processIntent(ProfileIntent.EditImageProfile(uri))
        }
    }

    LaunchedEffect(Unit) {
        profileViewModel.effect.collect { effect ->
            when (effect) {
                is ProfileUiEffect.OpenGallery -> {
                    imagePickerLauncher.launch("image/*")
                }
                is ProfileUiEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    if (!uiState.errorMessage.isNullOrEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = uiState.errorMessage.toString(), color = Color.Red)
        }
    }

    uiState.profile?.let { profile ->
        ProfileContent(
            profile = profile,
            onEditProfileClick = onEditProfileClick,
            onSettingsClick = onSettingsClick,
            onAvatarClick = { profileViewModel.dispatchGalleryEffect() }
        )
    }
}

@Composable
fun ProfileContent(
    profile: UserProfileDto,
    onEditProfileClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onAvatarClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // Hero / Profile header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF4F5F8))
        ) {
            Column {
                // Cover strip
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(96.dp)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFF1757F0), Color(0xFF3B82F6))
                            )
                        )
                ) {
                    IconButton(
                        onClick = onSettingsClick,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .background(
                                Color.Black.copy(alpha = 0.2f),
                                androidx.compose.foundation.shape.CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Name + info section
                Column(
                    modifier = Modifier.padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 56.dp,
                        bottom = 20.dp
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        Text(
                            text = profile.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.4).sp
                        )
                        if (profile.isVerified) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = "Verified",
                                tint = Color(0xFF1757F0),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "Edit profile",
                            color = Color(0xFF1757F0),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable { onEditProfileClick() }
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.padding(bottom = 10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Place,
                                contentDescription = "Location",
                                tint = Color(0xFF737880),
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = profile.location.ifEmpty { "Location not set" },
                                color = Color(0xFF737880),
                                fontSize = 13.sp
                            )
                        }
                        Text(text = "·", color = Color(0xFF737880))
                        val memberSince = Instant.ofEpochMilli(profile.createdAt)
                            .atZone(ZoneId.systemDefault())
                            .year
                        Text(
                            text = "Member since $memberSince",
                            color = Color(0xFF737880),
                            fontSize = 13.sp
                        )
                    }

                    Text(
                        text = profile.bio.ifEmpty { "No bio available." },
                        color = Color(0xFF737880),
                        fontSize = 13.sp,
                        lineHeight = 19.sp
                    )
                }
            }

            // Avatar + edit button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 68.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Box(modifier = Modifier.clickable { onAvatarClick() }) {
                    AsyncImage(
                        model = profile.avatarUrl,
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .border(3.dp, Color.White, RoundedCornerShape(24.dp))
                            .background(Color(0xFFF4F5F8)),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .align(Alignment.BottomEnd)
                            .offset(x = 4.dp, y = 4.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF1757F0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Edit",
                            tint = Color.White,
                            modifier = Modifier.size(10.dp)
                        )
                    }
                }

                if (profile.isVerified) {
                    Surface(
                        color = Color(0xFFE5EDFF),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = Color(0xFF1757F0),
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "ID Verified",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1757F0)
                            )
                        }
                    }
                }
            }
        }

        // Stats row
        Card(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .offset(y = (-10).dp)
        ) {
            val currentStats = listOf(
                Stat("Events", (profile.stats["attendedCount"] ?: 0).toString()),
                Stat("Hosted", (profile.stats["hostedCount"] ?: 0).toString()),
                Stat("Rating", (profile.stats["rating"] ?: 5.0).toString())
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                currentStats.forEachIndexed { index, stat ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 14.dp, horizontal = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stat.value,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.5).sp,
                            color = Color(0xFF0D0F14)
                        )
                        Text(
                            text = stat.label,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF737880),
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                    if (index < currentStats.size - 1) {
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(40.dp)
                                .align(Alignment.CenterVertically)
                                .background(Color(0xFF0D0F14).copy(alpha = 0.07f))
                        )
                    }
                }
            }
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
            // Achievements
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Achievements", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "See all",
                    color = Color(0xFF1757F0),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { }
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                BADGES.forEach { badge ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(badge.bg)
                            .padding(vertical = 12.dp, horizontal = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = badge.emoji, fontSize = 22.sp)
                        Text(
                            text = badge.label,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = badge.color,
                            textAlign = TextAlign.Center,
                            lineHeight = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Recent activity
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Recent activity", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "See all",
                    color = Color(0xFF1757F0),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { }
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                val RECENT = listOf(
                    "https://images.unsplash.com/photo-1552674605-db6ffd4facb5?w=120&h=120&fit=crop&auto=format",
                    "https://images.unsplash.com/photo-1533174072545-7a4b6ad7a6c3?w=120&h=120&fit=crop&auto=format",
                    "https://images.unsplash.com/photo-1517457373958-b7bdd4587205?w=120&h=120&fit=crop&auto=format",
                    "https://images.unsplash.com/photo-1492684223066-81342ee5ff30?w=120&h=120&fit=crop&auto=format"
                )
                items(RECENT) { src ->
                    AsyncImage(
                        model = src,
                        contentDescription = null,
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFF4F5F8)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Trust & Safety
            Surface(
                color = Color(0xFFF3F4F6),
                shape = RoundedCornerShape(24.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF1757F0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Trust profile",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1757F0)
                        )
                        Text(
                            text = "ID verified · 127 reviews · 98% response rate",
                            fontSize = 12.sp,
                            color = Color(0xFF4B5563),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            listOf("ID ✓", "Phone ✓", "Email ✓").forEach { tag ->
                                Surface(
                                    color = Color.White,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = tag,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1757F0),
                                        modifier = Modifier.padding(
                                            horizontal = 8.dp,
                                            vertical = 3.dp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileContent(
        profile = UserProfileDto(
            name = "Alex Johnson",
            email = "alex@example.com",
            location = "San Francisco, CA",
            bio = "Outdoor enthusiast and tech explorer.",
            isVerified = true
        )
    )
}
