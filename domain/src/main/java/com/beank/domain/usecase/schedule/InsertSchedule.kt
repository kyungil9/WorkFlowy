package com.beank.domain.usecase.schedule

import com.beank.domain.model.Schedule
import com.beank.domain.repository.ScheduleRepository
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

class InsertSchedule (
    private val scheduleRepository: ScheduleRepository
) {
    operator fun invoke(schedule: Schedule) = scheduleRepository.insertSchedule(schedule)
}