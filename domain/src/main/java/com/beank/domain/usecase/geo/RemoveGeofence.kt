package com.beank.domain.usecase.geo

import com.beank.domain.model.GeofenceData
import com.beank.domain.repository.GeofenceRepository
import javax.inject.Inject

class RemoveGeofence @Inject constructor(
    private val geofenceRepository: GeofenceRepository
) {
    operator fun invoke(id : String, onSuccess : () -> Unit = {}, onFail : () -> Unit = {})
            = geofenceRepository.removeGeofenceToClient(id, onSuccess, onFail)
}