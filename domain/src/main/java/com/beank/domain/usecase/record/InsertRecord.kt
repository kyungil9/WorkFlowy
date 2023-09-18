package com.beank.domain.usecase.record

import com.beank.domain.model.Record
import com.beank.domain.repository.RecordRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InsertRecord @Inject constructor(
    private val recordRepository: RecordRepository
){
    operator fun invoke(record: Record) = recordRepository.insertRecord(record)
}