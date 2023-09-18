package com.beank.domain.repository

import com.beank.domain.model.NowRecord
import com.beank.domain.model.Record
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface RecordRepository {

    fun getRecordInfo() : Flow<List<Record>>

    fun getTodayRecord(startDateTime: Long ,endDateTime: Long) : Flow<List<Record>>

    fun getPauseRecord(pause: Boolean) : Flow<NowRecord> //??수정?

    fun insertRecord(record: Record)

    fun updateRecord(id : String, endTime: LocalDateTime, progressTime : Long, pause : Boolean)
}