package com.beank.domain.usecase.geo

import com.beank.domain.model.GeofenceData
import com.beank.domain.repository.GeofenceRepository
import javax.inject.Inject

class AddGeofence @Inject constructor(
    private val geofenceRepository: GeofenceRepository
) {
    operator fun invoke(geofenceData: GeofenceData, onSuccess : () -> Unit = {}, onFail : () -> Unit = {})
        = geofenceRepository.addGeofenceToClient(geofenceData, onSuccess, onFail)
    operator fun invoke(geofenceList: List<GeofenceData>, onSuccess : () -> Unit = {}, onFail : () -> Unit = {})
        = geofenceRepository.addGeofenceListToClient(geofenceList,onSuccess,onFail)
}