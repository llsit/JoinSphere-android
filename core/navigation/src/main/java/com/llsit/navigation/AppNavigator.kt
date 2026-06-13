package com.llsit.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack

@Stable
class AppNavigator(
    val rootBackStack: NavBackStack<NavKey>,
    val homeBackStack: NavBackStack<NavKey>,
    val searchBackStack: NavBackStack<NavKey>,
    val createBackStack: NavBackStack<NavKey>,
    val eventsBackStack: NavBackStack<NavKey>,
    val profileBackStack: NavBackStack<NavKey>,
    initialTab: BottomTab = BottomTab.Home
) {
    // Current active tab (JoinSphere จะเริ่มที่หน้า Home เป็นหน้าแรก)
    var currentTab by mutableStateOf(initialTab)

    val activeTabBackStack: NavBackStack<NavKey>
        get() = when (currentTab) {
            BottomTab.Home -> homeBackStack
            BottomTab.Search -> searchBackStack
            BottomTab.Create -> createBackStack
            BottomTab.Events -> eventsBackStack
            BottomTab.Profile -> profileBackStack
        }

    // ── Tab switching & Visibility ───────────────────────────────────────────
    val shouldShowBottomBar: Boolean
        get() {
            if (rootBackStack.lastOrNull() !is MainKey) return false
            val key = activeTabBackStack.lastOrNull()
            // แสดง BottomBar เฉพาะเวลาที่อยู่หน้า Root ของแต่ละแท็บเท่านั้น
            return key is DiscoverKey ||
                    key is SearchKey ||
                    key is CreateEventKey ||
                    key is MyActivitiesKey ||
                    key is ProfileKey
        }

    fun switchTab(tab: BottomTab) {
        currentTab = tab
    }

    // ── Navigation actions ────────────────────────────────────────────────────

    fun navigateTo(key: NavKey) = activeTabBackStack.add(key)

    fun goBack() {
        if (activeTabBackStack.size > 1) {
            activeTabBackStack.removeLastOrNull()
        }
    }

    fun popToRoot() {
        val root = activeTabBackStack.firstOrNull() ?: return
        activeTabBackStack.clear()
        activeTabBackStack.add(root)
    }

    // ── Flow Shortcuts (Splash → Onboarding → Auth → Main) ────────────────────

    fun proceedFromSplash(startOnboarding: Boolean) {
        rootBackStack.clear()
        if (startOnboarding) {
            rootBackStack.add(OnboardingKey)
        } else {
            rootBackStack.add(AuthKey)
        }
    }

    fun completeOnboarding() {
        rootBackStack.clear()
        rootBackStack.add(AuthKey)
    }

    fun loginSuccess() {
        rootBackStack.clear()
        rootBackStack.add(MainKey) // สลับเข้าสู่หน้าหลักของแอป
    }

    fun logout() {
        rootBackStack.clear()
        rootBackStack.add(AuthKey)
    }

    // ── JoinSphere Shortcut Helpers ───────────────────────────────────────────

    // เปิดหน้าค้นหา
    fun openSearch() = navigateTo(SearchKey)

    // เปิดหน้ารายละเอียดอีเวนต์ในแท็บปัจจุบันที่ผู้ใช้กำลังใช้งานอยู่
    fun openEventDetail(eventId: String) = navigateTo(EventDetailKey(eventId))

    // เปิดหน้าสร้างอีเวนต์
    fun openCreateEvent() = navigateTo(CreateEventKey)

    // เปิดห้องแชท
    fun openChatRoom(chatId: String, roomName: String) = navigateTo(ChatRoomKey(chatId, roomName))

    // เปิดหน้าแจ้งเตือน
    fun openNotifications() = navigateTo(NotificationsKey)

    // เปิดหน้าตั้งค่า (มักจะเปิดจากหน้า Profile)
    fun openSettings() = navigateTo(SettingsKey)
}

// CompositionLocal เพื่อให้ทุก Composable เรียกใช้งาน Navigator ได้ง่ายๆ
val LocalNavigator = compositionLocalOf<AppNavigator> {
    error("No AppNavigator provided")
}

@Composable
fun rememberAppNavigator(): AppNavigator {
    val rootBackStack = rememberNavBackStack(SplashKey)

    val homeBackStack = rememberNavBackStack(DiscoverKey)
    val searchBackStack = rememberNavBackStack(SearchKey)
    val createBackStack = rememberNavBackStack(CreateEventKey)
    val eventsBackStack = rememberNavBackStack(MyActivitiesKey)
    val profileBackStack = rememberNavBackStack(ProfileKey)
    
    val currentTab = rememberSaveable { mutableStateOf(BottomTab.Home) }

    return remember(rootBackStack, homeBackStack, searchBackStack, createBackStack, eventsBackStack, profileBackStack) {
        AppNavigator(
            rootBackStack = rootBackStack,
            homeBackStack = homeBackStack,
            searchBackStack = searchBackStack,
            createBackStack = createBackStack,
            eventsBackStack = eventsBackStack,
            profileBackStack = profileBackStack,
            initialTab = currentTab.value
        ).apply {
            // Synchronize the navigator's state with the saved state
            this.currentTab = currentTab.value
        }
    }.also {
        // Keep the saved state in sync
        currentTab.value = it.currentTab
    }
}
