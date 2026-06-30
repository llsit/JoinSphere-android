package com.llsit.joinsphere.feature.search

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

data class SearchEvent(
    val id: Int,
    val title: String,
    val category: String,
    val date: String,
    val time: String,
    val location: String,
    val attendees: Int,
    val price: String,
    val soloPercent: Int,
    val rating: Float,
    val verified: Boolean,
    val image: String
)

val ALL_EVENTS = listOf(
    SearchEvent(
        1,
        "Golden Gate Morning Run",
        "Sports",
        "Sat, Jun 14",
        "7:00 AM",
        "Golden Gate Park",
        34,
        "Free",
        71,
        4.9f,
        true,
        "https://images.unsplash.com/photo-1552674605-db6ffd4facb5?w=300&h=200&fit=crop&auto=format"
    ),
    SearchEvent(
        2,
        "Rooftop Jazz & Wine Night",
        "Music",
        "Fri, Jun 13",
        "7:30 PM",
        "SoMa Rooftop",
        82,
        "$25",
        58,
        4.8f,
        true,
        "https://images.unsplash.com/photo-1415201364774-f6f0bb35f28f?w=300&h=200&fit=crop&auto=format"
    ),
    SearchEvent(
        3,
        "Sunday Farmers Market",
        "Food & Drink",
        "Sun, Jun 15",
        "9:00 AM",
        "Ferry Building",
        150,
        "Free",
        82,
        4.7f,
        true,
        "https://images.unsplash.com/photo-1488459716781-31db52582fe9?w=300&h=200&fit=crop&auto=format"
    ),
    SearchEvent(
        4,
        "Pottery Workshop",
        "Arts",
        "Thu, Jun 19",
        "2:00 PM",
        "Mission Arts Center",
        12,
        "$45",
        90,
        5.0f,
        true,
        "https://images.unsplash.com/photo-1565193566173-7a0ee3dbe261?w=300&h=200&fit=crop&auto=format"
    ),
    SearchEvent(
        5,
        "Sunset Yoga on the Beach",
        "Outdoors",
        "Wed, Jun 18",
        "5:30 PM",
        "Ocean Beach",
        28,
        "$10",
        76,
        4.9f,
        false,
        "https://images.unsplash.com/photo-1506126613408-eca07ce68773?w=300&h=200&fit=crop&auto=format"
    ),
    SearchEvent(
        6,
        "React Developers Meetup",
        "Tech",
        "Mon, Jun 23",
        "6:30 PM",
        "Salesforce Tower",
        64,
        "Free",
        68,
        4.6f,
        true,
        "https://images.unsplash.com/photo-1540575467063-178a50c2df87?w=300&h=200&fit=crop&auto=format"
    ),
    SearchEvent(
        7,
        "Street Food Festival",
        "Food & Drink",
        "Sat, Jun 14",
        "11:00 AM",
        "Embarcadero",
        320,
        "Free",
        79,
        4.8f,
        true,
        "https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=300&h=200&fit=crop&auto=format"
    ),
    SearchEvent(
        8,
        "Indie Film Screening",
        "Arts",
        "Fri, Jun 20",
        "8:00 PM",
        "Castro Theatre",
        95,
        "$15",
        63,
        4.5f,
        false,
        "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=300&h=200&fit=crop&auto=format"
    )
)

val CATEGORIES = listOf("All", "Sports", "Music", "Food & Drink", "Arts", "Outdoors", "Tech")
val PRICE_OPTS = listOf("Any price", "Free only", "Paid only")
val SORT_OPTS = listOf("Relevance", "Date", "Rating", "Attendees")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(onEventClick: (String, String?, String?) -> Unit = { _, _, _ -> }) {
    var query by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("All") }
    var priceFilter by remember { mutableStateOf("Any price") }
    var sortOrder by remember { mutableStateOf("Relevance") }
    var showSoloFriendlyOnly by remember { mutableStateOf(false) }

    val filteredEvents = remember(query, category, priceFilter, showSoloFriendlyOnly) {
        ALL_EVENTS.filter { ev ->
            val matchQuery = query.isEmpty() || ev.title.contains(query, ignoreCase = true) ||
                    ev.location.contains(query, ignoreCase = true) ||
                    ev.category.contains(query, ignoreCase = true)
            val matchCategory = category == "All" || ev.category == category
            val matchPrice =
                priceFilter == "Any price" || (if (priceFilter == "Free only") ev.price == "Free" else ev.price != "Free")
            val matchSolo = !showSoloFriendlyOnly || ev.soloPercent >= 70
            matchQuery && matchCategory && matchPrice && matchSolo
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Column(
            modifier = Modifier.padding(
                start = 20.dp,
                end = 20.dp,
                top = 16.dp,
                bottom = 12.dp
            )
        ) {
            Text(
                text = "Search",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp,
                modifier = Modifier.padding(bottom = 14.dp)
            )

            // Search bar
            TextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                placeholder = {
                    Text(
                        "Activities, venues, interests...",
                        color = Color(0xFF737880),
                        fontSize = 15.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Search,
                        contentDescription = null,
                        tint = Color(0xFF737880),
                        modifier = Modifier.size(18.dp)
                    )
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { query = "" }) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Clear",
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF3F4F6),
                    focusedContainerColor = Color(0xFFF3F4F6),
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )
        }

        // Filter row
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 14.dp)
        ) {
            item {
                // Solo friendly toggle
                FilterChip(
                    selected = showSoloFriendlyOnly,
                    onClick = { showSoloFriendlyOnly = !showSoloFriendlyOnly },
                    label = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(13.dp)
                            )
                            Text("Solo-friendly", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF1757F0),
                        selectedLabelColor = Color.White,
                        selectedLeadingIconColor = Color.White
                    ),
                    border = null
                )
            }

            item {
                DropDownFilter(
                    label = priceFilter,
                    options = PRICE_OPTS,
                    onSelected = { priceFilter = it },
                    isSelected = priceFilter != "Any price"
                )
            }

            item {
                DropDownFilter(
                    label = "Sort: $sortOrder",
                    options = SORT_OPTS,
                    onSelected = { sortOrder = it },
                    isSelected = false
                )
            }
        }

        // Category chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 14.dp)
        ) {
            items(CATEGORIES) { c ->
                val isSelected = category == c
                Surface(
                    onClick = { category = c },
                    shape = RoundedCornerShape(20.dp),
                    color = if (isSelected) Color(0xFF0D0F14) else Color.Transparent,
                    border = BorderStroke(
                        1.5.dp,
                        if (isSelected) Color(0xFF0D0F14) else Color(0xFF0D0F14).copy(alpha = 0.1f)
                    )
                ) {
                    Text(
                        text = c,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.White else Color(0xFF737880)
                    )
                }
            }
        }

        // Results count
        Text(
            text = "${filteredEvents.size} event${if (filteredEvents.size != 1) "s" else ""} found",
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF737880)
        )

        // Results
        if (filteredEvents.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(64.dp),
                        shape = RoundedCornerShape(24.dp),
                        color = Color(0xFFF3F4F6)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = Color(0xFF737880),
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("No events found", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "Try adjusting your filters",
                            fontSize = 14.sp,
                            color = Color(0xFF737880)
                        )
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                filteredEvents.forEach { ev ->
                    SearchResultCard(
                        event = ev,
                        onClick = { onEventClick(ev.id.toString(), ev.title, ev.image) })
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun SearchResultCard(event: SearchEvent, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.98f else 1f, label = "scale")

    Surface(
        onClick = onClick,
        interactionSource = interactionSource,
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
    ) {
        Row(modifier = Modifier.height(110.dp)) {
            Box(modifier = Modifier.width(96.dp)) {
                AsyncImage(
                    model = event.image,
                    contentDescription = event.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                if (event.verified) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(22.dp)
                            .align(Alignment.BottomStart),
                        shadowElevation = 2.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = Color(0xFF1757F0),
                                modifier = Modifier.size(11.dp)
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(vertical = 13.dp, horizontal = 14.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = event.category,
                        color = Color(0xFF1757F0),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = event.price,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (event.price == "Free") Color(0xFF16A34A) else Color(0xFF0D0F14)
                    )
                }

                Text(
                    text = event.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.2).sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = Color(0xFF737880),
                        modifier = Modifier.size(11.dp)
                    )
                    Text(
                        text = "${event.date} · ${event.time}",
                        color = Color(0xFF737880),
                        fontSize = 12.sp
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color(0xFF1757F0),
                            modifier = Modifier.size(11.dp)
                        )
                        Text(
                            text = "${event.soloPercent}% came solo",
                            color = Color(0xFF1757F0),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFF59E0B),
                            modifier = Modifier.size(11.dp)
                        )
                        Text(
                            text = event.rating.toString(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DropDownFilter(
    label: String,
    options: List<String>,
    onSelected: (String) -> Unit,
    isSelected: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Surface(
            onClick = { expanded = true },
            shape = RoundedCornerShape(20.dp),
            color = if (isSelected) Color(0xFF0D0F14) else Color(0xFFF4F5F8)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = label,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color.White else Color(0xFF737880)
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = if (isSelected) Color.White else Color(0xFF737880),
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    SearchScreen()
}
