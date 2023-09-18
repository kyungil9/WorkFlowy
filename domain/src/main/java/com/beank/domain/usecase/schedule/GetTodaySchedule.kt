package com.beank.domain.usecase.schedule

import com.beank.domain.repository.ScheduleRepository
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetTodaySchedule @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) {
    operator fun invoke(today: LocalDate) = scheduleRepository.getScheduleInfo(today)
}