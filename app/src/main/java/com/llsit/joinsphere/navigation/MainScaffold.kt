package com.llsit.joinsphere.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.llsit.joinsphere.core.design.MainBottomBar
import com.llsit.joinsphere.core.design.PlaceholderScreen
import com.llsit.joinsphere.feature.createevent.CreateEventScreen
import com.llsit.joinsphere.feature.discover.DiscoverScreen
import com.llsit.joinsphere.feature.eventdetail.EventDetailScreen
import com.llsit.joinsphere.feature.myevents.MyEventsScreen
import com.llsit.joinsphere.feature.profile.ProfileScreen
import com.llsit.joinsphere.feature.search.SearchScreen
import com.llsit.joinsphere.feature.settings.SettingsScreen
import com.llsit.navigation.AppNavigator
import com.llsit.navigation.ChatKey
import com.llsit.navigation.ChatRoomKey
import com.llsit.navigation.CreateEventKey
import com.llsit.navigation.DiscoverKey
import com.llsit.navigation.EventDetailKey
import com.llsit.navigation.MapKey
import com.llsit.navigation.MyActivitiesKey
import com.llsit.navigation.NotificationsKey
import com.llsit.navigation.ProfileKey
import com.llsit.navigation.SearchKey
import com.llsit.navigation.SettingsKey

@Composable
fun MainScaffold(
    navigator: AppNavigator,
) {
    val navEntries = remember(navigator) { mainNavEntries(navigator) }

    Scaffold(
        bottomBar = {
            if (navigator.shouldShowBottomBar) {
                MainBottomBar(navigator = navigator)
            }
        }
    ) { paddingValues ->
        NavDisplay(
            backStack = navigator.activeTabBackStack,
            onBack = { navigator.goBack() },
            modifier = Modifier.padding(paddingValues),
            entryProvider = navEntries
        )
    }
}

private fun mainNavEntries(
    navigator: AppNavigator
): (NavKey) -> NavEntry<NavKey> {
    return entryProvider {

        // ── [Tab: Home] ───────────────────────────────────────────────────
        entry<DiscoverKey> {
            DiscoverScreen(
                onEventClick = { id -> navigator.openEventDetail(id.toString()) },
                onNotificationClick = { navigator.openNotifications() },
                onSearchClick = { navigator.openSearch() }
            )
        }

        entry<SearchKey> {
            SearchScreen(
                onEventClick = { id -> navigator.openEventDetail(id.toString()) }
            )
        }

        // ── [Tab: Create] ─────────────────────────────────────────────────
        entry<CreateEventKey> {
            CreateEventScreen()
        }

        // ── [Tab: Events] ─────────────────────────────────────────────────
        entry<MyActivitiesKey> {
            MyEventsScreen(
                onEventClick = { id -> navigator.openEventDetail(id) },
                onCreateEventClick = { navigator.openCreateEvent() },
                onChatClick = { id -> navigator.openChatRoom(id, "Event Chat") }
            )
        }

        // ── [Tab: Profile] ────────────────────────────────────────────────
        entry<ProfileKey> {
            ProfileScreen(
                onLogout = {
                    navigator.navigateToAuth()
                },
                onSettingsClick = { navigator.openSettings() }
            )
        }

        // ── [Global/Detail Screens] ───────────────────────────────────────
        entry<EventDetailKey> {
            EventDetailScreen(
                onBackClick = { navigator.goBack() },
                onJoinClick = { /* Handle join flow */ }
            )
        }

        entry<ChatRoomKey> { chatKey ->
            PlaceholderScreen(name = "Chat Room: ${chatKey.roomName}")
        }

        entry<NotificationsKey> {
            PlaceholderScreen(name = "Notifications")
        }

        entry<SettingsKey> {
            SettingsScreen(
                onBackClick = { navigator.goBack() },
                onLogout = { navigator.navigateToAuth() }
            )
        }

        entry<MapKey> {
            PlaceholderScreen(name = "Map View")
        }

        entry<ChatKey> {
            PlaceholderScreen(name = "Chat List")
        }
    }
}
