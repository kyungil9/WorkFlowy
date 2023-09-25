package com.beank.domain.usecase.record

import com.beank.domain.repository.RecordRepository
import java.time.LocalDate
import javax.inject.Inject

class GetPeriodRecord @Inject constructor(
    private val recordRepository: RecordRepository
) {
    operator fun invoke(startDate: LocalDate, endDate: LocalDate) = recordRepository.getPeriodRecord(startDate, endDate)
}