package com.beank.domain.usecase.record

import com.beank.domain.model.Record
import com.beank.domain.model.Tag
import com.beank.domain.repository.RecordRepository
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

class StartNewRecord @Inject constructor(
    private val recordRepository: RecordRepository
){
    operator fun invoke(id: String, endTime: LocalDateTime, progressTime: Long, pause: Boolean, record: Record) {
        recordRepository.updateRecord(id,endTime,progressTime,pause)
        recordRepository.insertRecord(record)
    }
}