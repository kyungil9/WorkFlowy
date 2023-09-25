package com.beank.domain.usecase.record

import com.beank.domain.repository.RecordRepository
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

class GetTodayRecord @Inject constructor(
    private val recordRepository: RecordRepository
){
    operator fun invoke(date : LocalDate) =
        recordRepository.getTodayRecord(date)
}