package com.beank.domain.repository

import com.beank.domain.model.FireStoreState
import com.beank.domain.model.NowRecord
import com.beank.domain.model.Record
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime

interface RecordRepository {

    fun getRecordInfo() : Flow<FireStoreState<List<Record>>>

    fun getPeriodRecord(startDate: LocalDate,endDate: LocalDate) : Flow<FireStoreState<List<Record>>>

    suspend fun getPauseRecord(pause: Boolean) : Flow<FireStoreState<NowRecord>> //??수정?

    suspend fun getNowRecord() : NowRecord

    suspend fun getCurrentRecord() : Record

    fun insertRecord(record: Record)

    fun updateRecord(id : String, endTime: LocalDateTime, progressTime : Long, pause : Boolean)

    fun updateRecord(id : String, startTime: LocalDateTime, endTime: LocalDateTime, progressTime : Long, pause : Boolean, date: LocalDate)

    fun updateRecord(id : String, startTime: LocalDateTime, endTime: LocalDateTime, progressTime : Long, date: LocalDate)
}