package com.beank.domain.usecase

import com.beank.domain.usecase.record.GetCurrentRecord
import com.beank.domain.usecase.record.GetNowRecord
import com.beank.domain.usecase.record.InsertRecord
import com.beank.domain.usecase.record.UpdateRecord
import com.beank.domain.usecase.setting.GetMoveState
import com.beank.domain.usecase.setting.GetRecordAlarm
import com.beank.domain.usecase.setting.UpdateRecordAlarm

data class RecordAlarmUsecases (
    val getNowRecord: GetNowRecord,
    val getCurrentRecord: GetCurrentRecord,
    val updateRecord: UpdateRecord,
    val insertRecord: InsertRecord,
    val getRecordAlarm: GetRecordAlarm
)