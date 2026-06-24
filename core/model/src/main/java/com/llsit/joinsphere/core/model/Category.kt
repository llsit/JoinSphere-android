package com.llsit.joinsphere.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: String,
    val label: String,
    val emoji: String
)
