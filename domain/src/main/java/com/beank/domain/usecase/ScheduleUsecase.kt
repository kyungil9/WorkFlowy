package com.beank.domain.usecase

import com.beank.domain.model.Schedule
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ScheduleUsecase {

    fun getScheduleInfo(today : LocalDate) : Flow<List<Schedule>>

    fun insertSchedule(schedule: Schedule)

    fun updateSchedule(schedule: Schedule)

    fun deleteSchedule(schedule: Schedule)

}