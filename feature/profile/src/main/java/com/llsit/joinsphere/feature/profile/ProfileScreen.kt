package com.llsit.joinsphere.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.llsit.joinsphere.core.design.Button
import com.llsit.joinsphere.core.design.ButtonVariant
import com.llsit.joinsphere.core.design.Card

data class Stat(val label: String, val value: String)
data class Badge(val emoji: String, val label: String, val bg: Color, val color: Color)

val STATS = listOf(
    Stat("Attended", "47"),
    Stat("Hosted", "8"),
    Stat("Reviews", "4.9★")
)

val BADGES = listOf(
    Badge("🏃", "Active Runner", Color(0xFFEFF6FF), Color(0xFF1D4ED8)),
    Badge("🎵", "Music Fan", Color(0xFFF5F3FF), Color(0xFF6D28D9)),
    Badge("⭐", "Top Host", Color(0xFFFFFBEB), Color(0xFFD97706)),
    Badge("🤝", "Connector", Color(0xFFECFDF5), Color(0xFF059669))
)

val RECENT = listOf(
    "https://images.unsplash.com/photo-1552674605-db6ffd4facb5?w=120&h=120&fit=crop&auto=format",
    "https://images.unsplash.com/photo-1415201364774-f6f0bb35f28f?w=120&h=120&fit=crop&auto=format",
    "https://images.unsplash.com/photo-1565193566173-7a0ee3dbe261?w=120&h=120&fit=crop&auto=format",
    "https://images.unsplash.com/photo-1488521787991-ed7bbaae773c?w=120&h=120&fit=crop&auto=format",
    "https://images.unsplash.com/photo-1540575467063-178a50c2df87?w=120&h=120&fit=crop&auto=format"
)

@Composable
fun ProfileScreen(
    onLogout: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
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
                )

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
                            text = "Alex Johnson",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.4).sp
                        )
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "Verified",
                            tint = Color(0xFF1757F0),
                            modifier = Modifier.size(16.dp)
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
                                text = "Mission District, SF",
                                color = Color(0xFF737880),
                                fontSize = 13.sp
                            )
                        }
                        Text(text = "·", color = Color(0xFF737880))
                        Text(
                            text = "Member since 2023",
                            color = Color(0xFF737880),
                            fontSize = 13.sp
                        )
                    }

                    Text(
                        text = "Outdoor enthusiast, amateur chef, always looking for the next great experience. Here to explore the city and meet interesting people.",
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
                Box {
                    AsyncImage(
                        model = "https://images.unsplash.com/photo-1527980965255-d3b416303d12?w=160&h=160&fit=crop&auto=format",
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

        // Stats row
        Card(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .offset(y = (-10).dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                STATS.forEachIndexed { index, stat ->
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
                    if (index < STATS.size - 1) {
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

            // Settings link
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Settings", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "Edit profile",
                    color = Color(0xFF1757F0),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onSettingsClick() }
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSettingsClick() }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF3F4F6)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                            tint = Color(0xFF737880),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Account settings",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Notifications, privacy and more",
                            fontSize = 12.sp,
                            color = Color(0xFF737880)
                        )
                    }
                    Icon(
                        imageVector = Icons.Outlined.ChevronRight,
                        contentDescription = null,
                        tint = Color(0xFFCBD0D8),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Logout
            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                variant = ButtonVariant.Outline
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFFDC2626)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Sign out",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFDC2626)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}
