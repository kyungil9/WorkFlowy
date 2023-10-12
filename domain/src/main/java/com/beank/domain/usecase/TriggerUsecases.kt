package com.beank.domain.usecase

import com.beank.domain.usecase.geo.AddGeofence
import com.beank.domain.usecase.geo.GetGeoTriggerList
import com.beank.domain.usecase.geo.RemoveGeofence
import com.beank.domain.usecase.geo.UpdateGeofence

data class TriggerUsecases (
    val addGeofence: AddGeofence,
    val removeGeofence: RemoveGeofence,
    val updateGeofence: UpdateGeofence,
    val getGeoTriggerList: GetGeoTriggerList
)