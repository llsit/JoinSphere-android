package com.llsit.joinsphere.feature.createevent

import android.annotation.SuppressLint
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.tasks.CancellationTokenSource
import com.llsit.joinsphere.feature.createevent.state.LocationPickerIntent
import com.llsit.joinsphere.feature.createevent.state.LocationPickerUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocationPickerViewModel(
    private val fusedLocationClient: FusedLocationProviderClient,
    private val geocoder: Geocoder
) : ViewModel() {

    private val _uiState = MutableStateFlow(LocationPickerUiState())
    val uiState: StateFlow<LocationPickerUiState> = _uiState.asStateFlow()

    fun processIntent(intent: LocationPickerIntent) {
        when (intent) {
            is LocationPickerIntent.UpdateSearchQuery -> {
                _uiState.update { it.copy(searchQuery = intent.query) }
            }
            is LocationPickerIntent.SearchLocation -> searchLocation(intent.query)
            is LocationPickerIntent.UpdateCenter -> {
                _uiState.update { it.copy(centerLatitude = intent.lat, centerLongitude = intent.lng) }
                resolveAddress(intent.lat, intent.lng)
            }
            is LocationPickerIntent.UserLocationFound -> {
                _uiState.update { it.copy(centerLatitude = intent.lat, centerLongitude = intent.lng) }
                resolveAddress(intent.lat, intent.lng)
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(onLocationFound: (Double, Double) -> Unit) {
        try {
            fusedLocationClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)
                .addOnSuccessListener { location ->
                    if (location != null) {
                        onLocationFound(location.latitude, location.longitude)
                        processIntent(LocationPickerIntent.UserLocationFound(location.latitude, location.longitude))
                    }
                }
        } catch (e: SecurityException) {
            // Handle permission error
        }
    }

    private fun resolveAddress(lat: Double, lng: Double) {
        viewModelScope.launch {
            _uiState.update { it.copy(isResolvingAddress = true) }
            withContext(Dispatchers.IO) {
                try {
                    val addresses = geocoder.getFromLocation(lat, lng, 1)
                    if (!addresses.isNullOrEmpty()) {
                        val address = addresses[0]
                        val addressLine = address.getAddressLine(0) ?: "$lat, $lng"
                        _uiState.update {
                            it.copy(
                                selectedPlace = it.selectedPlace.copy(
                                    name = address.featureName ?: address.thoroughfare ?: addressLine,
                                    address = addressLine,
                                    latitude = lat,
                                    longitude = lng
                                ),
                                isResolvingAddress = false
                            )
                        }
                    } else {
                        updateFallbackAddress(lat, lng)
                    }
                } catch (e: Exception) {
                    updateFallbackAddress(lat, lng)
                }
            }
        }
    }

    private fun updateFallbackAddress(lat: Double, lng: Double) {
        _uiState.update {
            it.copy(
                selectedPlace = it.selectedPlace.copy(
                    name = "$lat, $lng",
                    address = "$lat, $lng",
                    latitude = lat,
                    longitude = lng
                ),
                isResolvingAddress = false
            )
        }
    }

    private fun searchLocation(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isSearching = true) }
            withContext(Dispatchers.IO) {
                try {
                    val addresses = geocoder.getFromLocationName(query, 1)
                    if (!addresses.isNullOrEmpty()) {
                        val address = addresses[0]
                        _uiState.update {
                            it.copy(
                                centerLatitude = address.latitude,
                                centerLongitude = address.longitude,
                                isSearching = false
                            )
                        }
                        resolveAddress(address.latitude, address.longitude)
                    }
                } catch (e: Exception) {
                    _uiState.update { it.copy(isSearching = false) }
                } finally {
                    _uiState.update { it.copy(isSearching = false) }
                }
            }
        }
    }
}
