package com.beank.domain.usecase.record

import com.beank.domain.repository.RecordRepository
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

class UpdateRecord(
    private val recordRepository: RecordRepository
){
    operator fun invoke(id: String, endTime: LocalDateTime, progressTime : Long, pause : Boolean) = recordRepository.updateRecord(id,endTime,progressTime,pause)
}