package com.beank.domain.usecase

import com.beank.domain.usecase.geo.AddGeofence
import com.beank.domain.usecase.geo.UpdateGeofence
import com.beank.domain.usecase.tag.GetAllTag

data class TriggerSettingUsecases(
    val updateGeofence: UpdateGeofence,
    val addGeofence: AddGeofence,
    val getAllTag: GetAllTag
)