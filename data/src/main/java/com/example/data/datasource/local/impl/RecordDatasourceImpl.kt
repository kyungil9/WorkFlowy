package com.example.data.datasource.local.impl

import com.example.data.datasource.local.RecordDatasource
import com.example.data.datasource.local.database.dao.WeekRecordDao
import com.example.data.datasource.local.database.entity.WeekRecord
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import javax.inject.Inject

class RecordDatasourceImpl @Inject constructor(
    private val recordService : WeekRecordDao
) : RecordDatasource {

    override fun getRecordInfo(): Flow<List<WeekRecord>> = recordService.getAllRecordInfo()

    override fun getTodayRecord(startDateTime: Long, endDateTime: Long): Flow<List<WeekRecord>> = recordService.getTodayRecordInfo(startDateTime, endDateTime)

    override fun getPauseRecord(pause: Boolean): Flow<List<WeekRecord>> = recordService.getSelectRecordInfo(pause)

    override fun getRecordSize(): Int = recordService.getRecordSize()

    override fun insertRecord(record: WeekRecord) {
        recordService.insertRecord(record)
    }

    override fun updateRecord(endTime: LocalDateTime, progressTime: Long, id: Int, pause: Boolean) {
        recordService.updateRecord(endTime,progressTime,id,pause)
    }
}