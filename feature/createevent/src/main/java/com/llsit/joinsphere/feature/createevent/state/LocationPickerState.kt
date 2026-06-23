package com.llsit.joinsphere.feature.createevent.state

import com.llsit.joinsphere.core.model.event.SelectedPlace

data class LocationPickerUiState(
    val selectedPlace: SelectedPlace = SelectedPlace(),
    val isResolvingAddress: Boolean = false,
    val centerLatitude: Double = 13.7563,
    val centerLongitude: Double = 100.5018,
    val searchQuery: String = "",
    val isSearching: Boolean = false
)

sealed interface LocationPickerIntent {
    data class UpdateSearchQuery(val query: String) : LocationPickerIntent
    data class SearchLocation(val query: String) : LocationPickerIntent
    data class UpdateCenter(val lat: Double, val lng: Double) : LocationPickerIntent
    data class UserLocationFound(val lat: Double, val lng: Double) : LocationPickerIntent
}