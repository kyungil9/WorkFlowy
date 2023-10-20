package com.beank.domain.usecase

import com.beank.domain.usecase.geo.GetChooseGeofence
import com.beank.domain.usecase.geo.StartGeofenceToClient
import com.beank.domain.usecase.record.GetCurrentRecord
import com.beank.domain.usecase.record.InsertRecord
import com.beank.domain.usecase.record.UpdateRecord
import com.beank.domain.usecase.setting.GetGeoState
import com.beank.domain.usecase.setting.GetTriggerToggle
import com.beank.domain.usecase.setting.UpdateGeoState

data class GeoUsecases(
    val getCurrentRecord: GetCurrentRecord,
    val updateRecord: UpdateRecord,
    val insertRecord: InsertRecord,
    val getChooseGeofence: GetChooseGeofence,
    val getGeoState: GetGeoState,
    val updateGeoState: UpdateGeoState,
    val getTriggerToggle: GetTriggerToggle,
    val startGeofenceToClient: StartGeofenceToClient
)