package com.beank.domain.usecase

import com.beank.domain.usecase.geo.GetChooseGeofence
import com.beank.domain.usecase.geo.RemoveGeofence
import com.beank.domain.usecase.geo.StartGeofenceToClient
import com.beank.domain.usecase.geo.StartMoveToClient
import com.beank.domain.usecase.record.GetCurrentRecord
import com.beank.domain.usecase.record.InsertRecord
import com.beank.domain.usecase.record.UpdateRecord
import com.beank.domain.usecase.setting.GetGeoState
import com.beank.domain.usecase.setting.GetMoveState
import com.beank.domain.usecase.setting.GetMoveTriggerToggle
import com.beank.domain.usecase.setting.GetTriggerToggle
import com.beank.domain.usecase.setting.UpdateGeoState
import com.beank.domain.usecase.setting.UpdateMoveState

data class GeoUsecases(
    val getCurrentRecord: GetCurrentRecord,
    val updateRecord: UpdateRecord,
    val insertRecord: InsertRecord,
    val getChooseGeofence: GetChooseGeofence,
    val getGeoState: GetGeoState,
    val updateGeoState: UpdateGeoState,
    val getTriggerToggle: GetTriggerToggle,
    val getMoveTriggerToggle: GetMoveTriggerToggle,
    val startGeofenceToClient: StartGeofenceToClient,
    val startMoveToClient: StartMoveToClient,
    val getMoveState: GetMoveState,
    val updateMoveState: UpdateMoveState,
    val removeGeofence: RemoveGeofence
)