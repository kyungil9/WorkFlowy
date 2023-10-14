package com.beank.domain.usecase

import com.beank.domain.usecase.geo.GetGeoTriggerList
import com.beank.domain.usecase.geo.RemoveGeofence

data class TriggerUsecases (
    val removeGeofence: RemoveGeofence,
    val getGeoTriggerList: GetGeoTriggerList
)