package com.beank.domain.usecase.record

import com.beank.domain.repository.RecordRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

class UpdateRecord @Inject constructor(
    private val recordRepository: RecordRepository
){
    operator fun invoke(id: String, endTime: LocalDateTime, progressTime : Long, pause : Boolean) = recordRepository.updateRecord(id,endTime,progressTime,pause)
    operator fun invoke(id: String, startTime: LocalDateTime, endTime: LocalDateTime, progressTime : Long, pause : Boolean, date: LocalDate) = recordRepository.updateRecord(id, startTime, endTime, progressTime, pause, date)
    operator fun invoke(id: String, startTime: LocalDateTime, endTime: LocalDateTime, progressTime : Long, date: LocalDate) = recordRepository.updateRecord(id,startTime,endTime,progressTime,date)
}