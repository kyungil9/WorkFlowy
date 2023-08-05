package com.example.data.datasource.local

import com.example.data.datasource.local.database.entity.WeekSchedule
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ScheduleDatasource {

    fun getScheduleInfo(today : LocalDate) : Flow<List<WeekSchedule>>

    fun insertSchedule(schedule: WeekSchedule)

    fun updateSchedule(schedule: WeekSchedule)

    fun deleteSchedule(schedule: WeekSchedule)
}