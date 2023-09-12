package com.beank.data.repositoryimpl

import com.beank.data.datasource.local.ScheduleDatasource
import com.beank.data.datasource.local.mapper.toScheduleModel
import com.beank.data.datasource.local.mapper.toWeekSchedule
import com.beank.domain.model.Schedule
import com.beank.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val scheduleDatasource: ScheduleDatasource
) : ScheduleRepository {

    override fun getScheduleInfo(today: LocalDate): Flow<List<Schedule>> =
        scheduleDatasource.getScheduleInfo(today).map { scheduleList ->
            scheduleList.map { schedule ->
                schedule.toScheduleModel()
            }
        }

    override fun insertSchedule(schedule: Schedule) {
        scheduleDatasource.insertSchedule(schedule.toWeekSchedule())
    }

    override fun updateSchedule(schedule: Schedule) {
        scheduleDatasource.updateSchedule(schedule.toWeekSchedule())
    }

    override fun deleteSchedule(schedule: Schedule) {
        scheduleDatasource.deleteSchedule(schedule.toWeekSchedule())
    }
}