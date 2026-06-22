package com.llsit.joinsphere.core.domain.usecase

import com.llsit.joinsphere.core.domain.repository.LocationRepository

data class UserLocation(
    val lat: Double,
    val lng: Double,
    val address: String
)

class GetCurrentLocationUseCase(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(): UserLocation? {
        val location = locationRepository.getCurrentLocation() ?: return null
        val address = locationRepository.getAddressFromLocation(location.latitude, location.longitude)
            ?: "${location.latitude}, ${location.longitude}"
        
        return UserLocation(
            lat = location.latitude,
            lng = location.longitude,
            address = address
        )
    }
}
