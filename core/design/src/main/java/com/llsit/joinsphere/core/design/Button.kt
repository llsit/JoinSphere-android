package com.llsit.joinsphere.core.design

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

enum class ButtonVariant {
    Default, Destructive, Outline, Secondary, Ghost, Link
}

enum class ButtonSize {
    Default, Sm, Lg, Icon
}

@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.Default,
    size: ButtonSize = ButtonSize.Default,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    val containerColor = when (variant) {
        ButtonVariant.Default -> MaterialTheme.colorScheme.primary
        ButtonVariant.Destructive -> MaterialTheme.colorScheme.error
        ButtonVariant.Secondary -> MaterialTheme.colorScheme.secondaryContainer
        ButtonVariant.Outline, ButtonVariant.Ghost, ButtonVariant.Link -> Color.Transparent
    }

    val contentColor = when (variant) {
        ButtonVariant.Default -> MaterialTheme.colorScheme.onPrimary
        ButtonVariant.Destructive -> MaterialTheme.colorScheme.onError
        ButtonVariant.Secondary -> MaterialTheme.colorScheme.onSecondaryContainer
        ButtonVariant.Outline -> MaterialTheme.colorScheme.outline
        ButtonVariant.Ghost -> MaterialTheme.colorScheme.primary
        ButtonVariant.Link -> MaterialTheme.colorScheme.primary
    }

    val height = when (size) {
        ButtonSize.Default -> 40.dp
        ButtonSize.Sm -> 36.dp
        ButtonSize.Lg -> 44.dp
        ButtonSize.Icon -> 40.dp
    }

    val padding = when (size) {
        ButtonSize.Default -> PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ButtonSize.Sm -> PaddingValues(horizontal = 12.dp, vertical = 6.dp)
        ButtonSize.Lg -> PaddingValues(horizontal = 24.dp, vertical = 10.dp)
        ButtonSize.Icon -> PaddingValues(0.dp)
    }

    val shape = RoundedCornerShape(8.dp)

    val border = if (variant == ButtonVariant.Outline) {
        BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    } else null

    Surface(
        onClick = onClick,
        modifier = modifier
            .height(height)
            .then(if (size == ButtonSize.Icon) Modifier.size(height) else Modifier),
        enabled = enabled,
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        border = border,
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            val textStyle = if (variant == ButtonVariant.Link) {
                MaterialTheme.typography.labelLarge.copy(textDecoration = TextDecoration.Underline)
            } else {
                MaterialTheme.typography.labelLarge
            }

            CompositionLocalProvider(LocalTextStyle provides textStyle) {
                Row(
                    Modifier
                        .defaultMinSize(
                            minWidth = if (size == ButtonSize.Icon) height else ButtonDefaults.MinHeight
                        )
                        .padding(padding),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    content()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = {}) { Text("Default Button") }
            Button(onClick = {}, variant = ButtonVariant.Destructive) { Text("Destructive") }
            Button(onClick = {}, variant = ButtonVariant.Outline) { Text("Outline") }
            Button(onClick = {}, variant = ButtonVariant.Secondary) { Text("Secondary") }
            Button(onClick = {}, variant = ButtonVariant.Ghost) { Text("Ghost") }
            Button(onClick = {}, variant = ButtonVariant.Link) { Text("Link Button") }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {}, size = ButtonSize.Sm) { Text("Small") }
                Button(onClick = {}, size = ButtonSize.Default) { Text("Default") }
                Button(onClick = {}, size = ButtonSize.Lg) { Text("Large") }
            }
        }
    }
}
