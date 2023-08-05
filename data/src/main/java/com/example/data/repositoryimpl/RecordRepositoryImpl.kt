package com.example.data.repositoryimpl

import com.example.data.datasource.local.RecordDatasource
import com.example.data.datasource.local.mapper.toRecordModel
import com.example.data.datasource.local.mapper.toWeekRecord
import com.example.domain.model.Record
import com.example.domain.repository.RecordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

class RecordRepositoryImpl @Inject constructor(
    private val recordDatasource: RecordDatasource
) : RecordRepository {

    override fun getRecordInfo(): Flow<List<Record>> =
        recordDatasource.getRecordInfo().map { recordList ->
            recordList.map { record ->
                record.toRecordModel()
            }
        }

    override fun getTodayRecord(startDateTime: Long, endDateTime: Long): Flow<List<Record>> =
        recordDatasource.getTodayRecord(startDateTime, endDateTime).map { recordList ->
            recordList.map { record ->
                record.toRecordModel()
            }
        }

    override fun getPauseRecord(pause: Boolean): Flow<List<Record>> =
        recordDatasource.getPauseRecord(pause).map { recordList ->
            recordList.map { record ->
                record.toRecordModel()
            }
        }

    override fun insertRecord(record: Record) {
        recordDatasource.insertRecord(record.toWeekRecord())
    }

    override fun updateRecord(endTime: LocalDateTime, progressTime: Long, id: Int, pause: Boolean) {
        recordDatasource.updateRecord(endTime, progressTime, id, pause)
    }
}