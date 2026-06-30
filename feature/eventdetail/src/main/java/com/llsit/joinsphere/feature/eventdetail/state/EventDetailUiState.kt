package com.llsit.joinsphere.feature.eventdetail.state

import com.llsit.joinsphere.core.model.Category
import com.llsit.joinsphere.core.model.UserProfileDto
import com.llsit.joinsphere.core.model.event.EventDto

data class EventDetailUiState(
    val isLoading: Boolean = false,
    val event: EventDto? = null,
    val host: UserProfileDto? = null,
    val categories: List<Category> = emptyList(),
    val error: String? = null
)
