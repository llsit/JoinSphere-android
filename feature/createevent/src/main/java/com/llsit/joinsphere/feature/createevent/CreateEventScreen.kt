package com.llsit.joinsphere.feature.createevent

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

data class Category(val id: String, val label: String, val emoji: String)

val CATEGORIES = listOf(
    Category("sports", "Sports", "🏃"),
    Category("music", "Music", "🎵"),
    Category("food", "Food", "🍽️"),
    Category("arts", "Arts", "🎨"),
    Category("outdoors", "Outdoors", "🌲"),
    Category("tech", "Tech", "💻"),
    Category("social", "Social", "🤝"),
    Category("wellness", "Wellness", "🧘")
)

@Composable
fun CreateEventScreen() {
    var step by remember { mutableStateOf(1) }
    var submitted by remember { mutableStateOf(false) }

    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var maxAttendees by remember { mutableStateOf("") }
    var isFree by remember { mutableStateOf(true) }
    var price by remember { mutableStateOf("") }
    var soloFriendly by remember { mutableStateOf(true) }
    var imageSet by remember { mutableStateOf(false) }

    val step1Complete = title.trim().length > 3 && category.isNotEmpty()
    val step2Complete = date.isNotEmpty() && time.isNotEmpty() && location.trim().isNotEmpty()

    if (submitted) {
        SuccessScreen(onReset = {
            submitted = false
            step = 1
            title = ""
            category = ""
            description = ""
            date = ""
            time = ""
            location = ""
            maxAttendees = ""
            isFree = true
            price = ""
            soloFriendly = true
            imageSet = false
        }, location = location)
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // Header
            HeaderSection(
                step = step,
                onBack = { if (step > 1) step -= 1 }
            )

            // Progress Bar
            ProgressBar(step = step)

            // Step Content
            Box(modifier = Modifier.weight(1f)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp)
                ) {
                    when (step) {
                        1 -> Step1(
                            title = title,
                            onTitleChange = { title = it },
                            category = category,
                            onCategoryChange = { category = it },
                            description = description,
                            onDescriptionChange = { description = it },
                            soloFriendly = soloFriendly,
                            onSoloFriendlyChange = { soloFriendly = it },
                            imageSet = imageSet,
                            onImageSetChange = { imageSet = it }
                        )

                        2 -> Step2(
                            date = date,
                            onDateChange = { date = it },
                            time = time,
                            onTimeChange = { time = it },
                            location = location,
                            onLocationChange = { location = it },
                            maxAttendees = maxAttendees,
                            onMaxAttendeesChange = { maxAttendees = it },
                            isFree = isFree,
                            onIsFreeChange = { isFree = it },
                            price = price,
                            onPriceChange = { price = it }
                        )

                        3 -> Step3(
                            title = title,
                            category = category,
                            description = description,
                            date = date,
                            time = time,
                            location = location,
                            maxAttendees = maxAttendees,
                            isFree = isFree,
                            price = price,
                            soloFriendly = soloFriendly
                        )
                    }
                    Spacer(modifier = Modifier.height(120.dp))
                }
            }

            // Bottom CTA
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.95f))
                    .border(1.dp, Color(0xFF0D0F14).copy(alpha = 0.07f))
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Button(
                    onClick = { if (step < 3) step += 1 else submitted = true },
                    enabled = (step == 1 && step1Complete) || (step == 2 && step2Complete) || step == 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1757F0))
                ) {
                    Text(
                        text = if (step < 3) "Continue" else "Publish event",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun SuccessScreen(onReset: () -> Unit, location: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            modifier = Modifier.size(72.dp),
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF1757F0),
            shadowElevation = 8.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Event published!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.5).sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Your event is now live and visible to people near ${location.ifEmpty { "your area" }}.",
            fontSize = 14.sp,
            color = Color(0xFF737880),
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onReset,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1757F0))
        ) {
            Text("Create another event", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun HeaderSection(step: Int, onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (step > 1) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF3F4F6))
            ) {
                Icon(
                    Icons.Outlined.ChevronLeft,
                    contentDescription = "Back",
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        Column {
            Text(
                text = when (step) {
                    1 -> "Create event"
                    2 -> "Time & place"
                    else -> "Review & publish"
                },
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp
            )
            Text(text = "Step $step of 3", fontSize = 13.sp, color = Color(0xFF737880))
        }
    }
}

@Composable
fun ProgressBar(step: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        for (s in 1..3) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(if (s <= step) Color(0xFF1757F0) else Color(0xFFF4F5F8))
            )
        }
    }
}

@Composable
fun Step1(
    title: String, onTitleChange: (String) -> Unit,
    category: String, onCategoryChange: (String) -> Unit,
    description: String, onDescriptionChange: (String) -> Unit,
    soloFriendly: Boolean, onSoloFriendlyChange: (Boolean) -> Unit,
    imageSet: Boolean, onImageSetChange: (Boolean) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        // Photo upload
        Surface(
            onClick = { onImageSetChange(!imageSet) },
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFFF4F5F8)
        ) {
            if (imageSet) {
                Box {
                    AsyncImage(
                        model = "https://images.unsplash.com/photo-1552674605-db6ffd4facb5?w=600&h=280&fit=crop&auto=format",
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            color = Color.White.copy(alpha = 0.95f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                "Change photo",
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Surface(
                        modifier = Modifier.size(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        shadowElevation = 2.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Outlined.CameraAlt,
                                contentDescription = null,
                                tint = Color(0xFF737880),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Add a cover photo",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF737880)
                    )
                    Text(
                        "Helps people find your event",
                        fontSize = 12.sp,
                        color = Color(0xFF737880)
                    )
                }
            }
        }

        // Title
        FormField(label = "Event title") {
            OutlinedTextField(
                value = title,
                onValueChange = onTitleChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Give it a clear, catchy name", color = Color(0xFF9CA3AF)) },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF3F4F6),
                    focusedContainerColor = Color(0xFFF3F4F6),
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent
                )
            )
        }

        // Category
        Column {
            Text(
                "Category",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 10.dp)
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                val chunkedCategories = CATEGORIES.chunked(4)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    chunkedCategories.forEach { rowCategories ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            rowCategories.forEach { cat ->
                                val active = category == cat.id
                                Surface(
                                    onClick = { onCategoryChange(cat.id) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(16.dp),
                                    color = if (active) Color(0xFFEDF2FF) else Color(0xFFF4F5F8),
                                    border = if (active) BorderStroke(
                                        1.5.dp,
                                        Color(0xFF1757F0)
                                    ) else null
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(vertical = 10.dp)
                                    ) {
                                        Text(text = cat.emoji, fontSize = 20.sp)
                                        Text(
                                            text = cat.label,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (active) Color(0xFF1757F0) else Color(
                                                0xFF737880
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Description
        FormField(label = "Description (optional)") {
            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                placeholder = {
                    Text(
                        "Tell people what to expect, what to bring, the vibe...",
                        color = Color(0xFF9CA3AF)
                    )
                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF3F4F6),
                    focusedContainerColor = Color(0xFFF3F4F6),
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent
                )
            )
            Text(
                text = "${description.length}/500",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                textAlign = TextAlign.End,
                fontSize = 12.sp,
                color = Color(0xFF737880)
            )
        }

        // Solo-friendly Toggle
        Surface(
            color = Color(0xFFF3F4F6),
            shape = RoundedCornerShape(24.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Solo-friendly event",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1757F0)
                    )
                    Text(
                        "Mark this as welcoming to solo attendees to build trust",
                        fontSize = 12.sp,
                        color = Color(0xFF4B5563)
                    )
                }
                Switch(
                    checked = soloFriendly,
                    onCheckedChange = onSoloFriendlyChange,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF1757F0),
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color(0xFFCBD0D8),
                        uncheckedBorderColor = Color.Transparent
                    )
                )
            }
        }
    }
}

@Composable
fun Step2(
    date: String, onDateChange: (String) -> Unit,
    time: String, onTimeChange: (String) -> Unit,
    location: String, onLocationChange: (String) -> Unit,
    maxAttendees: String, onMaxAttendeesChange: (String) -> Unit,
    isFree: Boolean, onIsFreeChange: (Boolean) -> Unit,
    price: String, onPriceChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            FormField(label = "Date", modifier = Modifier.weight(1f)) {
                CustomCreateTextField(
                    value = date,
                    onValueChange = onDateChange,
                    placeholder = "YYYY-MM-DD"
                )
            }
            FormField(label = "Start time", modifier = Modifier.weight(1f)) {
                CustomCreateTextField(
                    value = time,
                    onValueChange = onTimeChange,
                    placeholder = "HH:MM"
                )
            }
        }

        FormField(label = "Location") {
            CustomCreateTextField(
                value = location,
                onValueChange = onLocationChange,
                placeholder = "Address or venue name",
                leadingIcon = Icons.Default.Place
            )
        }

        FormField(label = "Capacity (optional)") {
            CustomCreateTextField(
                value = maxAttendees,
                onValueChange = onMaxAttendeesChange,
                placeholder = "Leave blank for unlimited",
                leadingIcon = Icons.Default.Group,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Column {
            Text(
                "Pricing",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 10.dp)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                listOf(true, false).forEach { free ->
                    Surface(
                        onClick = { onIsFreeChange(free) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        color = if (isFree == free) Color(0xFF0D0F14) else Color(0xFFF4F5F8)
                    ) {
                        Text(
                            text = if (free) "Free" else "Paid",
                            modifier = Modifier.padding(vertical = 13.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isFree == free) Color.White else Color(0xFF737880)
                        )
                    }
                }
            }
            if (!isFree) {
                CustomCreateTextField(
                    value = price,
                    onValueChange = onPriceChange,
                    placeholder = "0.00",
                    leadingIcon = Icons.Default.AttachMoney,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }
    }
}

@Composable
fun Step3(
    title: String,
    category: String,
    description: String,
    date: String,
    time: String,
    location: String,
    maxAttendees: String,
    isFree: Boolean,
    price: String,
    soloFriendly: Boolean
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Preview Card
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                AsyncImage(
                    model = "https://images.unsplash.com/photo-1552674605-db6ffd4facb5?w=600&h=260&fit=crop&auto=format",
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    contentScale = ContentScale.Crop
                )
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = CATEGORIES.find { it.id == category }?.label?.uppercase()
                            ?: "CATEGORY",
                        color = Color(0xFF1757F0),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = title.ifEmpty { "Event title" },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.3).sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    if (description.isNotEmpty()) {
                        Text(
                            text = description,
                            fontSize = 13.sp,
                            lineHeight = 20.sp,
                            color = Color(0xFF737880),
                            modifier = Modifier.padding(top = 6.dp),
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        // Details Summary
        Surface(
            color = Color(0xFFF3F4F6),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                val summaryItems = listOf(
                    Triple(Icons.Default.Schedule, date.ifEmpty { "No date set" }, time),
                    Triple(Icons.Default.Place, location.ifEmpty { "No location" }, ""),
                    Triple(
                        Icons.Default.Group,
                        if (maxAttendees.isEmpty()) "Unlimited capacity" else "Up to $maxAttendees people",
                        ""
                    ),
                    Triple(
                        Icons.Default.AttachMoney,
                        if (isFree) "Free event" else "$$price per person",
                        ""
                    )
                )

                summaryItems.forEachIndexed { index, item ->
                    val icon = item.first
                    val label = item.second
                    val sub = item.third
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = Color(0xFF1757F0),
                            modifier = Modifier.size(16.dp)
                        )
                        Column {
                            Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            if (sub.isNotEmpty()) {
                                Text(text = sub, fontSize = 12.sp, color = Color(0xFF737880))
                            }
                        }
                    }
                    if (index < summaryItems.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = Color(0xFF0D0F14).copy(alpha = 0.06f)
                        )
                    }
                }
            }
        }

        // Solo Friendly Indicator
        if (soloFriendly) {
            Surface(
                color = Color(0xFFE5EDFF),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        tint = Color(0xFF1757F0),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        "Marked as solo-friendly",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1757F0)
                    )
                }
            }
        }
    }
}

@Composable
fun FormField(label: String, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        content()
    }
}

@Composable
fun CustomCreateTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(placeholder, color = Color(0xFF9CA3AF), fontSize = 15.sp) },
        leadingIcon = leadingIcon?.let {
            {
                Icon(
                    it,
                    contentDescription = null,
                    tint = Color(0xFF737880),
                    modifier = Modifier.size(16.dp)
                )
            }
        },
        shape = RoundedCornerShape(16.dp),
        keyboardOptions = keyboardOptions,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color(0xFFF3F4F6),
            focusedContainerColor = Color(0xFFF3F4F6),
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent
        ),
        singleLine = true
    )
}

@Preview(showBackground = true)
@Composable
fun CreateEventScreenPreview() {
    CreateEventScreen()
}
