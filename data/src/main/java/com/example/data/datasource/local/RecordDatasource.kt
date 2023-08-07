package com.example.data.datasource.local

import com.example.data.datasource.local.database.entity.WeekRecord
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import java.time.LocalTime

interface RecordDatasource {

    fun getRecordInfo() : Flow<List<WeekRecord>>

    fun getTodayRecord(startDateTime: Long ,endDateTime: Long) : Flow<List<WeekRecord>>

    fun getPauseRecord(pause: Boolean) : Flow<List<WeekRecord>> //??수정?

    fun getRecordSize() : Int

    fun insertRecord(record: WeekRecord)

    fun updateRecord(endTime: LocalDateTime, progressTime : Long, id : Int, pause : Boolean)
}