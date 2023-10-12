package com.beank.domain.usecase

import com.beank.domain.usecase.geo.GetChooseGeofence
import com.beank.domain.usecase.record.GetCurrentRecord
import com.beank.domain.usecase.record.InsertRecord
import com.beank.domain.usecase.record.UpdateRecord

data class GeoUsecases(
    val getCurrentRecord: GetCurrentRecord,
    val updateRecord: UpdateRecord,
    val insertRecord: InsertRecord,
    val getChooseGeofence: GetChooseGeofence
)