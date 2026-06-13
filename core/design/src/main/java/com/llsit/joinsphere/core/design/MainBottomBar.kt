package com.llsit.joinsphere.core.design

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.llsit.navigation.AppNavigator
import com.llsit.navigation.BottomTab

@Composable
fun MainBottomBar(navigator: AppNavigator) {
    val activeBlue = Color(0xFF1757F0)
    val inactiveGray = Color(0xFF94A3B8)
    val activePillBg = Color(0xFFEFF6FF)

    Surface(
        shadowElevation = 16.dp,
        tonalElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        color = Color.White,
    ) {
        NavigationBar(
            containerColor = Color.White,
            tonalElevation = 0.dp,
            modifier = Modifier.height(88.dp),
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
