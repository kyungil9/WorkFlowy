package com.beank.domain.usecase.geo

import com.beank.domain.repository.GeofenceRepository
import javax.inject.Inject

class GetGeoTriggerList @Inject constructor(
    private val geofenceRepository: GeofenceRepository
) {
    operator fun invoke() = geofenceRepository.getGeoTriggerList()
}