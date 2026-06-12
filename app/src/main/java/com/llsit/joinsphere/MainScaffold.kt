package com.llsit.joinsphere

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.llsit.joinsphere.feature.createevent.CreateEventScreen
import com.llsit.joinsphere.feature.discover.DiscoverScreen
import com.llsit.joinsphere.feature.eventdetail.EventDetailScreen
import com.llsit.joinsphere.feature.profile.ProfileScreen
import com.llsit.navigation.AppNavigator
import com.llsit.navigation.BottomTab
import com.llsit.navigation.ChatKey
import com.llsit.navigation.ChatRoomKey
import com.llsit.navigation.CreateEventKey
import com.llsit.navigation.DiscoverKey
import com.llsit.navigation.EventDetailKey
import com.llsit.navigation.MapKey
import com.llsit.navigation.MyActivitiesKey
import com.llsit.navigation.NotificationsKey
import com.llsit.navigation.ProfileKey
import com.llsit.navigation.SettingsKey

@Composable
fun MainScaffold(navigator: AppNavigator) {
    Scaffold(
        bottomBar = {
            if (navigator.shouldShowBottomBar) {
                NavigationBar {
                    BottomTab.entries.forEach { tab ->
                        val isSelected = navigator.currentTab == tab
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                if (isSelected) {
                                    navigator.popToRoot()
                                } else {
                                    navigator.switchTab(tab)
                                }
                            },
                            icon = { Icon(imageVector = tab.icon, contentDescription = tab.label) },
                            label = { Text(text = tab.label) }
                        )
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

                // ── [Tab 1] Discover Flow ─────────────────────────────────────
                entry<DiscoverKey> {
                    DiscoverScreen(
                        onEventClick = { },
                    )
                }

                // ── [Tab 2] Map Flow ──────────────────────────────────────────
                entry<MapKey> {

                }

                // ── [Tab 3] Chat Flow ─────────────────────────────────────────
                entry<ChatKey> {

                }

                // ── [Tab 4] Activities Flow ───────────────────────────────────
                entry<MyActivitiesKey> {

                }

                // ── [Tab 5] Profile Flow ──────────────────────────────────────
                entry<ProfileKey> {
                    ProfileScreen(

                    )
                }

                // ── Detail & Utilities Screens (เปิดซ้อนอยู่ด้านบนของแท็บ) ──────────
                entry<EventDetailKey> { entry ->
//                    // ดึงค่า Arguments ออกมาใช้งานโดยตรงจาก Key คลาส
//                    val eventId = entry.key.eventId
//                    EventDetailScreen(eventId = eventId)
                }

                entry<CreateEventKey> {
                    CreateEventScreen()
                }

                entry<ChatRoomKey> { entry ->

                }

                entry<NotificationsKey> {

                }

                entry<SettingsKey> {

                }
            }
        )
    }
}