package com.llsit.joinsphere.core.design

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.llsit.navigation.AppNavigator
import com.llsit.navigation.BottomTab

@Composable
fun MainBottomBar(navigator: AppNavigator) {
    val activeBlue = Color(0xFF1757F0)
    val activePillBg = Color(0xFFEDF2FF)
    val inactiveGray = Color(0xFF9CA3AF)
    val borderColor = Color(0x0F0D0F14) // rgba(13,15,20,0.06)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        HorizontalDivider(color = borderColor, thickness = 1.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 20.dp, start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomTab.entries.forEach { tab ->
                val active = navigator.currentTab == tab
                val isCreate = tab == BottomTab.Create

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            if (active) {
                                navigator.popToRoot()
                            } else {
                                navigator.switchTab(tab)
                            }
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (isCreate) {
                        Surface(
                            modifier = Modifier
                                .offset(y = (-4).dp)
                                .size(width = 52.dp, height = 32.dp),
                            shape = RoundedCornerShape(16.dp),
                            color = activeBlue,
                            shadowElevation = 8.dp // Approximate 0 4px 12px rgba(23,87,240,0.32)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = tab.label,
                                    modifier = Modifier.size(18.dp),
                                    tint = Color.White
                                )
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .size(width = 52.dp, height = 32.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (active) activePillBg else Color.Transparent),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.label,
                                modifier = Modifier.size(20.dp),
                                tint = if (active) activeBlue else inactiveGray
                            )
                        }
                    }

                    Text(
                        text = tab.label,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isCreate || active) activeBlue else inactiveGray
                    )
                }
            }
        }
    }
}
