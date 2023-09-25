package com.beank.domain.usecase.schedule

import com.beank.domain.model.Schedule
import com.beank.domain.repository.ScheduleRepository
import javax.inject.Inject
import javax.inject.Singleton

class UpdateSchedule @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) {
    operator fun invoke(schedule: Schedule) = scheduleRepository.updateSchedule(schedule)
}