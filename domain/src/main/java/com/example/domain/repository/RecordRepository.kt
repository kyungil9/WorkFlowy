package com.example.domain.repository

import com.example.domain.model.Record
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface RecordRepository {

    fun getRecordInfo() : Flow<List<Record>>

    fun getTodayRecord(startDateTime: Long ,endDateTime: Long) : Flow<List<Record>>

    fun getPauseRecord(pause: Boolean) : Flow<List<Record>> //??수정?

    fun insertRecord(record: Record)

    fun updateRecord(endTime: LocalDateTime, progressTime : Long, id : Int, pause : Boolean)
}