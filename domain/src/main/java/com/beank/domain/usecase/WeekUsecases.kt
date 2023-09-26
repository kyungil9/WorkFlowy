package com.beank.domain.usecase

import com.beank.domain.usecase.record.GetNowRecord
import com.beank.domain.usecase.record.InsertRecord
import com.beank.domain.usecase.record.StartNewRecord
import com.beank.domain.usecase.record.UpdateRecord
import com.beank.domain.usecase.schedule.DeleteSchedule
import com.beank.domain.usecase.schedule.GetTodaySchedule
import com.beank.domain.usecase.schedule.UpdateCheckSchedule
import com.beank.domain.usecase.tag.DeleteTag
import com.beank.domain.usecase.tag.GetAllTag

data class WeekUsecases(
    val getAllTag: GetAllTag,
    val getTodaySchedule: GetTodaySchedule,
    val getNowRecord: GetNowRecord,
    val startNewRecord: StartNewRecord,
    val deleteTag: DeleteTag,
    val deleteSchedule: DeleteSchedule,
    val updateCheckSchedule: UpdateCheckSchedule,
    val updateRecord: UpdateRecord,
    val insertRecord: InsertRecord
)