package com.llsit.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

// ===== 1. Pre-Auth Flow =====
@Serializable
data object SplashKey : NavKey

@Serializable
data object OnboardingKey : NavKey

@Serializable
data object AuthKey : NavKey

@Serializable
data object MainKey : NavKey


// ===== 2. Main Dashboard / Tabs =====
@Serializable
data object DiscoverKey : NavKey

@Serializable
data object SearchKey : NavKey

@Serializable
data object MapKey : NavKey

@Serializable
data object ChatKey : NavKey // หน้าหลักสำหรับรวมรายการแชท

@Serializable
data object MyActivitiesKey : NavKey

@Serializable
data object NotificationsKey : NavKey

@Serializable
data object ProfileKey : NavKey


// ===== 3. Sub-Features & Details (รองรับการส่งข้อมูล / Arguments) =====

// สำหรับหน้าแสดงรายละเอียดอีเวนต์ ต้องรู้ว่าคืออีเวนต์ไหน
@Serializable
data class EventDetailKey(val eventId: String) : NavKey

// สำหรับหน้าสร้างอีเวนต์ใหม่
@Serializable
data object CreateEventKey : NavKey

// สำหรับหน้าห้องแชทส่วนตัว/กลุ่ม (ส่ง ID และชื่อห้องไปแสดงผล)
@Serializable
data class ChatRoomKey(val chatId: String, val roomName: String) : NavKey


// ===== 4. App Settings =====

@Serializable
data object SettingsKey : NavKey