package com.beank.domain.usecase.geo

import com.beank.domain.repository.GeofenceRepository
import javax.inject.Inject

class GetTempGeoTrigger @Inject constructor(
    private val geofenceRepository: GeofenceRepository
) {
    suspend operator fun invoke() = geofenceRepository.getTempGeoTrigger()
}