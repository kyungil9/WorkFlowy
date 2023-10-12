package com.beank.domain.usecase.geo

import com.beank.domain.repository.GeofenceRepository
import javax.inject.Inject

class StartGeofenceToClient @Inject constructor(
    private val geofenceRepository: GeofenceRepository
) {
    suspend operator fun invoke(onSuccess : () -> Unit = {}, onFail : () -> Unit = {}) = geofenceRepository.startGeofenceToClient(onSuccess, onFail)
}