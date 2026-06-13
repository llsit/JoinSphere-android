package com.llsit.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey

enum class BottomTab(
    val label: String,
    val icon: ImageVector,
    val rootKey: NavKey
) {
    Home("Home", Icons.Outlined.Home, DiscoverKey),
    Search("Search", Icons.Outlined.Search, SearchKey),
    Create("Create", Icons.Outlined.Add, CreateEventKey),
    Events("Events", Icons.Outlined.CalendarMonth, MyActivitiesKey),
    Profile("Profile", Icons.Outlined.Person, ProfileKey)
}
