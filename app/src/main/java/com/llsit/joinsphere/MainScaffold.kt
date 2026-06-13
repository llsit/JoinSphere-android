package com.llsit.joinsphere

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.llsit.joinsphere.feature.createevent.CreateEventScreen
import com.llsit.joinsphere.feature.discover.DiscoverScreen
import com.llsit.joinsphere.feature.eventdetail.EventDetailScreen
import com.llsit.joinsphere.feature.myevents.MyEventsScreen
import com.llsit.joinsphere.feature.profile.ProfileScreen
import com.llsit.joinsphere.feature.search.SearchScreen
import com.llsit.navigation.AppNavigator
import com.llsit.navigation.BottomTab
import com.llsit.navigation.ChatKey
import com.llsit.navigation.ChatRoomKey
import com.llsit.navigation.CreateEventKey
import com.llsit.navigation.DiscoverKey
import com.llsit.navigation.EventDetailKey
import com.llsit.navigation.FocusKey
import com.llsit.navigation.InsightsKey
import com.llsit.navigation.MapKey
import com.llsit.navigation.MyActivitiesKey
import com.llsit.navigation.NotificationsKey
import com.llsit.navigation.ProfileKey
import com.llsit.navigation.SearchKey
import com.llsit.navigation.SettingsKey

@Composable
fun PlaceholderScreen(name: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF737880)
        )
    }
}

@Composable
fun MainScaffold(navigator: AppNavigator) {
    val activeBlue = Color(0xFF1757F0)
    val inactiveGray = Color(0xFF94A3B8)
    val activePillBg = Color(0xFFEFF6FF)

    Scaffold(
        bottomBar = {
            if (navigator.shouldShowBottomBar) {
                Surface(
                    shadowElevation = 16.dp,
                    tonalElevation = 8.dp,
                    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                    color = Color.White
                ) {
                    NavigationBar(
                        containerColor = Color.White,
                        tonalElevation = 0.dp,
                        modifier = Modifier.height(88.dp)
                    ) {
                        BottomTab.entries.forEach { tab ->
                            val isSelected = navigator.currentTab == tab
                            
                            if (tab == BottomTab.Create) {
                                // Special "Create" button in the middle
                                NavigationBarItem(
                                    selected = isSelected,
                                    onClick = { navigator.switchTab(tab) },
                                    icon = {
                                        Surface(
                                            modifier = Modifier.size(48.dp),
                                            shape = RoundedCornerShape(20.dp),
                                            color = activeBlue,
                                            shadowElevation = 4.dp
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = tab.icon,
                                                    contentDescription = tab.label,
                                                    tint = Color.White,
                                                    modifier = Modifier.size(24.dp)
                                                )
                                            }
                                        }
                                    },
                                    label = {
                                        Text(
                                            text = tab.label,
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) activeBlue else inactiveGray
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        indicatorColor = Color.Transparent
                                    )
                                )
                            } else {
                                NavigationBarItem(
                                    selected = isSelected,
                                    onClick = {
                                        if (isSelected) {
                                            navigator.popToRoot()
                                        } else {
                                            navigator.switchTab(tab)
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = tab.icon,
                                            contentDescription = tab.label,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = tab.label,
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = activeBlue,
                                        selectedTextColor = activeBlue,
                                        unselectedIconColor = inactiveGray,
                                        unselectedTextColor = inactiveGray,
                                        indicatorColor = activePillBg
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        NavDisplay(
            backStack = navigator.activeTabBackStack,
            onBack = { navigator.goBack() },
            modifier = Modifier.padding(paddingValues),
            entryProvider = entryProvider {

                // ── [Tab 1] Home Flow ──────────────────────────────────────────
                entry<DiscoverKey> {
                    DiscoverScreen(
                        onEventClick = { id -> navigator.openEventDetail(id.toString()) },
                        onNotificationClick = { navigator.openNotifications() },
                        onSearchClick = { navigator.openSearch() }
                    )
                }

                // ── [Tab 2] Search Flow ────────────────────────────────────────
                entry<SearchKey> {
                    SearchScreen(
                        onEventClick = { id -> navigator.openEventDetail(id.toString()) }
                    )
                }

                // ── [Tab 3] Create Flow ────────────────────────────────────────
                entry<CreateEventKey> {
                    CreateEventScreen()
                }

                // ── [Tab 4] Events Flow ────────────────────────────────────────
                entry<MyActivitiesKey> {
                    MyEventsScreen(
                        onEventClick = { id -> navigator.openEventDetail(id) },
                        onCreateEventClick = { navigator.openCreateEvent() },
                        onChatClick = { id -> navigator.openChatRoom(id, "Event Chat") }
                    )
                }

                // ── [Tab 5] Profile Flow ──────────────────────────────────────
                entry<ProfileKey> {
                    ProfileScreen(
                        onLogout = { navigator.logout() }
                    )
                }

                // ── Detail & Utilities Screens ──────────────────────────────────
                entry<EventDetailKey> {
                    EventDetailScreen(
                        onBackClick = { navigator.goBack() },
                        onJoinClick = { /* Handle join */ }
                    )
                }

                entry<ChatRoomKey> { chatKey ->
                    PlaceholderScreen("Chat Room: ${chatKey.roomName}")
                }

                entry<NotificationsKey> {
                    PlaceholderScreen("Notifications")
                }

                entry<SettingsKey> {
                    PlaceholderScreen("Settings")
                }

                entry<FocusKey> {
                    PlaceholderScreen("Focus")
                }

                entry<InsightsKey> {
                    PlaceholderScreen("Insights")
                }
                
                entry<MapKey> {
                    PlaceholderScreen("Map View")
                }
                
                entry<ChatKey> {
                    PlaceholderScreen("Chat List")
                }
            }
        )
    }
}
