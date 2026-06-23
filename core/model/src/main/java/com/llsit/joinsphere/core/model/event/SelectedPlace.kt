package com.llsit.joinsphere.core.model.event

data class SelectedPlace(
    val name: String = "",
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)