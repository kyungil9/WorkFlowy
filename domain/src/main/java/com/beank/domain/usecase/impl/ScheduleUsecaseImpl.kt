package com.beank.domain.usecase.impl

import com.beank.domain.model.Schedule
import com.beank.domain.repository.ScheduleRepository
import com.beank.domain.usecase.ScheduleUsecase
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class ScheduleUsecaseImpl @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) : ScheduleUsecase {

    override fun getScheduleInfo(today: LocalDate): Flow<List<Schedule>> = scheduleRepository.getScheduleInfo(today)

    override fun insertSchedule(schedule: Schedule) {
        scheduleRepository.insertSchedule(schedule)
    }

    override fun updateSchedule(schedule: Schedule) {
        scheduleRepository.updateSchedule(schedule)
    }

    override fun deleteSchedule(schedule: Schedule) {
        scheduleRepository.deleteSchedule(schedule)
    }
}