package com.beank.data.datasource.local.impl

import com.beank.data.datasource.local.ScheduleDatasource
import com.beank.data.datasource.local.database.dao.WeekScheduleDao
import com.beank.data.datasource.local.database.entity.WeekSchedule
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class ScheduleDatasourceImpl @Inject constructor(
    private val scheduleService : WeekScheduleDao
) : ScheduleDatasource {

    override fun getScheduleInfo(today: LocalDate): Flow<List<WeekSchedule>> = scheduleService.getSearchSchedule(today)

    override fun insertSchedule(schedule: WeekSchedule) {
        scheduleService.insertSchedule(schedule)
    }

    override fun updateSchedule(schedule: WeekSchedule) {
        scheduleService.updateSchedule(schedule)
    }

    override fun deleteSchedule(schedule: WeekSchedule) {
        scheduleService.deleteSchedule(schedule)
    }
}