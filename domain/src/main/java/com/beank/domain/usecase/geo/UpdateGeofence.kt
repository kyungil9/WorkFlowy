package com.beank.domain.usecase.geo

import com.beank.domain.model.GeofenceData
import com.beank.domain.repository.GeofenceRepository
import javax.inject.Inject

class UpdateGeofence @Inject constructor(
    private val geofenceRepository: GeofenceRepository
) {
    operator fun invoke(geofenceData: GeofenceData, onSuccess : () -> Unit = {}, onFail : () -> Unit = {})
            = geofenceRepository.updateGeofenceToClient(geofenceData, onSuccess, onFail)
}