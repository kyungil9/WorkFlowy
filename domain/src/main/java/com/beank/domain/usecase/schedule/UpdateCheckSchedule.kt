package com.beank.domain.usecase.schedule

import com.beank.domain.repository.ScheduleRepository
import javax.inject.Inject

class UpdateCheckSchedule @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) {
    operator fun invoke(id : String, check : Boolean) = scheduleRepository.updateCheckSchedule(id, check)
}