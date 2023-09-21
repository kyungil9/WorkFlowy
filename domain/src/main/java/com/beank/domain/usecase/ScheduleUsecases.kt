package com.beank.domain.usecase

import com.beank.domain.usecase.schedule.InsertSchedule
import com.beank.domain.usecase.schedule.UpdateSchedule

data class ScheduleUsecases(
    val insertSchedule: InsertSchedule,
    val updateSchedule: UpdateSchedule
)
