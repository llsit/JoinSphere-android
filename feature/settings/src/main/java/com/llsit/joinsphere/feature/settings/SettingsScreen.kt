package com.llsit.joinsphere.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.llsit.joinsphere.core.design.Button
import com.llsit.joinsphere.core.design.ButtonVariant
import com.llsit.joinsphere.core.design.Card
import org.koin.androidx.compose.koinViewModel

data class MenuItem(
    val icon: ImageVector,
    val label: String,
    val sub: String,
    val hasToggle: Boolean = false,
    val onClick: () -> Unit = {}
)

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit = {},
    onLogout: () -> Unit = {},
    viewModel: SettingsViewModel = koinViewModel()
) {
    SettingsScreenContent(
        onBackClick = onBackClick,
        onLogout = onLogout,
        onResetOnboarding = { viewModel.resetOnboarding() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreenContent(
    onBackClick: () -> Unit = {},
    onLogout: () -> Unit = {},
    onResetOnboarding: () -> Unit = {}
) {
    var notificationsEnabled by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Outlined.ChevronLeft,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Card(
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Column {
                    val menuItems = listOf(
                        MenuItem(
                            Icons.Default.Notifications,
                            "Notifications",
                            "Customize alerts",
                            true
                        ),
                        MenuItem(
                            Icons.Default.Person,
                            "Solo preferences",
                            "Manage your comfort level"
                        ),
                        MenuItem(Icons.Default.Favorite, "Interests", "Sports, Music, Food +3"),
                        MenuItem(Icons.Default.Lock, "Privacy", "Data and visibility settings"),
                        MenuItem(
                            Icons.Default.Refresh,
                            "Reset Onboarding",
                            "Show onboarding on next restart",
                            onClick = onResetOnboarding
                        ),
                        MenuItem(Icons.Default.Help, "Help & Support", "FAQs and contact")
                    )

                    menuItems.forEachIndexed { index, item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { item.onClick() }
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
                                    imageVector = item.icon,
                                    contentDescription = null,
                                    tint = Color(0xFF737880),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.label,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(text = item.sub, fontSize = 12.sp, color = Color(0xFF737880))
                            }
                            if (item.hasToggle) {
                                Switch(
                                    checked = notificationsEnabled,
                                    onCheckedChange = { notificationsEnabled = it },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = Color(0xFF1757F0),
                                        uncheckedThumbColor = Color.White,
                                        uncheckedTrackColor = Color(0xFFCBD0D8),
                                        uncheckedBorderColor = Color.Transparent
                                    )
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Outlined.ChevronRight,
                                    contentDescription = null,
                                    tint = Color(0xFFCBD0D8),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        if (index < menuItems.size - 1) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = Color(0xFF0D0F14).copy(alpha = 0.06f)
                            )
                        }
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

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreenContent()
}
