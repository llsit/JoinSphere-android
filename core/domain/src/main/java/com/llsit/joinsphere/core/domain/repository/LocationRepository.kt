package com.llsit.joinsphere.core.domain.repository

import android.location.Location

interface LocationRepository {
    suspend fun getCurrentLocation(): Location?
    suspend fun getAddressFromLocation(lat: Double, lng: Double): String?
}
