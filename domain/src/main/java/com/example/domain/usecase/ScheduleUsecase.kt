package com.example.domain.usecase

import com.example.domain.model.Schedule
import com.example.domain.model.Tag
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ScheduleUsecase {

    fun getScheduleInfo(today : LocalDate) : Flow<List<Schedule>>

    fun insertSchedule(schedule: Schedule)

    fun updateSchedule(schedule: Schedule)

    fun deleteSchedule(schedule: Schedule)

}