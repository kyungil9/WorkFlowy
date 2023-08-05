package com.example.domain.repository

import com.example.domain.model.Schedule
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ScheduleRepository {

    fun getScheduleInfo(today : LocalDate) : Flow<List<Schedule>>

    fun insertSchedule(schedule: Schedule)

    fun updateSchedule(schedule: Schedule)

    fun deleteSchedule(schedule: Schedule)
}