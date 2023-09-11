package com.beank.domain.usecase.impl

import com.beank.domain.model.Record
import com.beank.domain.repository.RecordRepository
import com.beank.domain.usecase.RecordUsecase
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import javax.inject.Inject

class RecordUsecaseImpl @Inject constructor(
    private val recordRepository: RecordRepository
) : RecordUsecase {

    override fun getRecordInfo(): Flow<List<Record>> = recordRepository.getRecordInfo()

    override fun getTodayRecord(startDateTime: Long, endDateTime: Long): Flow<List<Record>> = recordRepository.getTodayRecord(startDateTime, endDateTime)

    override fun getPauseRecord(pause: Boolean): Flow<List<Record>> = recordRepository.getPauseRecord(pause)

    override fun getRecordSize(): Int = recordRepository.getRecordSize()

    override fun insertRecord(record: Record) {
        recordRepository.insertRecord(record)
    }

    override fun updateRecord(endTime: LocalDateTime, progressTime: Long, id: Int, pause: Boolean) {
        recordRepository.updateRecord(endTime, progressTime, id, pause)
    }
}