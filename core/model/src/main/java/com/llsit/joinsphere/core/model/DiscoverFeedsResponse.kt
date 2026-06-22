package com.llsit.joinsphere.core.model

import kotlinx.serialization.Serializable

@Serializable
data class DiscoverFeedsResponse(
    val trending: List<EventNetworkModel>,
    val thisWeek: List<EventNetworkModel>
)

@Serializable
data class EventNetworkModel(
    val id: String,
    val title: String,
    val category: String? = null,
    val score: Double = 0.0,
    val attendeesCount: Int = 0,
    val startTimestamp: String
)