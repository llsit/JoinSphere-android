package com.llsit.joinsphere.core.design

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A simple Accordion component for Jetpack Compose, 
 * following the structure of the provided React code.
 */

@Composable
fun Accordion(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier, content = { content() })
}

@Composable
fun AccordionItem(
    modifier: Modifier = Modifier,
    isExpanded: Boolean = false,
    onToggle: () -> Unit = {},
    trigger: @Composable (expanded: Boolean) -> Unit,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        AccordionTrigger(
            expanded = isExpanded,
            onClick = onToggle,
            content = { trigger(isExpanded) }
        )
        AccordionContent(
            expanded = isExpanded,
            content = content
        )
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 4.dp),
            thickness = 1.dp,
            color = Color.LightGray.copy(alpha = 0.3f)
        )
    }
}

@Composable
fun AccordionTrigger(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "ChevronRotation"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(modifier = Modifier.weight(1f)) {
            content()
        }
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            modifier = Modifier
                .size(20.dp)
                .rotate(rotation),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun AccordionContent(
    expanded: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = expanded,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically(),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            content()
        }
    }
}

/**
 * Example usage of the Accordion components.
 */
@Composable
fun AccordionExample() {
    var expandedItem by remember { mutableStateOf<Int?>(null) }

    Accordion(modifier = Modifier.padding(16.dp)) {
        repeat(3) { index ->
            AccordionItem(
                isExpanded = expandedItem == index,
                onToggle = { 
                    expandedItem = if (expandedItem == index) null else index 
                },
                trigger = {
                    Text(
                        text = "Is it accessible? Item $index",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            ) {
                Text(
                    text = "Yes. It adheres to the WAI-ARIA design pattern. This is some sample content for the accordion item that explains things in more detail.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )
            }
        }
    }
}
