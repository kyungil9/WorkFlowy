package com.beank.domain.repository


import com.beank.domain.model.FireStoreState
import com.beank.domain.model.Schedule
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ScheduleRepository {

    fun getScheduleInfo(today : LocalDate) : Flow<FireStoreState<List<Schedule>>>

    fun insertSchedule(schedule: Schedule)

    fun updateSchedule(schedule: Schedule)

    fun updateCheckSchedule(id : String, check : Boolean)

    fun deleteSchedule(schedule: Schedule)
}