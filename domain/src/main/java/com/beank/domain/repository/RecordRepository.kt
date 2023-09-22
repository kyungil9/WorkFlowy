package com.beank.domain.repository

import com.beank.domain.model.FireStoreState
import com.beank.domain.model.NowRecord
import com.beank.domain.model.Record
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime

interface RecordRepository {

    fun getRecordInfo() : Flow<FireStoreState<List<Record>>>

    fun getTodayRecord(date: LocalDate) : Flow<FireStoreState<List<Record>>>

    fun getPauseRecord(pause: Boolean) : Flow<FireStoreState<NowRecord>> //??수정?

    fun insertRecord(record: Record)

    fun updateRecord(id : String, endTime: LocalDateTime, progressTime : Long, pause : Boolean)
}