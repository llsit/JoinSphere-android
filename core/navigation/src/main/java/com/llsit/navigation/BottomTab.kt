package com.llsit.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey

enum class BottomTab(
    val label: String,
    val icon: ImageVector,
    val rootKey: NavKey
) {
    Discover("Discover", Icons.Outlined.Explore, DiscoverKey),
    Map("Map", Icons.Outlined.Map, MapKey),
    Chat("Chat", Icons.AutoMirrored.Outlined.Chat, ChatKey),
    MyActivities("Activities", Icons.Outlined.CalendarMonth, MyActivitiesKey),
    Profile("Profile", Icons.Outlined.Person, ProfileKey)
}