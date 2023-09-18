package com.beank.domain.usecase.record

import com.beank.domain.repository.RecordRepository
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetTodayRecord @Inject constructor(
    private val recordRepository: RecordRepository
){
    operator fun invoke(startDateTime: Long, endDateTime: Long) = recordRepository.getTodayRecord(startDateTime, endDateTime)
}