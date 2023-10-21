package com.beank.domain.usecase.schedule

import com.beank.domain.repository.ScheduleRepository
import java.time.LocalDate
import javax.inject.Inject

class GetAlarmSchedule @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) {
    suspend operator fun invoke(date: LocalDate) = scheduleRepository.getAlarmSchedule(date)
}