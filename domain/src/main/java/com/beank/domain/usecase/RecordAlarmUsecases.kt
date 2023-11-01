package com.beank.domain.usecase

import com.beank.domain.usecase.geo.AddGeofence
import com.beank.domain.usecase.geo.GetTempGeoTrigger
import com.beank.domain.usecase.geo.RemoveGeofence
import com.beank.domain.usecase.record.GetCurrentRecord
import com.beank.domain.usecase.record.GetNowRecord
import com.beank.domain.usecase.record.InsertRecord
import com.beank.domain.usecase.record.UpdateRecord
import com.beank.domain.usecase.setting.GetRecordAlarm
import com.beank.domain.usecase.tag.GetNextTag

data class RecordAlarmUsecases (
    val getNowRecord: GetNowRecord,
    val getCurrentRecord: GetCurrentRecord,
    val updateRecord: UpdateRecord,
    val insertRecord: InsertRecord,
    val getRecordAlarm: GetRecordAlarm,
    val addGeofence: AddGeofence,
    val getNextTag: GetNextTag,
    val removeGeofence: RemoveGeofence,
    val getTempGeoTrigger: GetTempGeoTrigger
)