package com.beank.data.repositoryimpl

import com.beank.data.datasource.local.RecordDatasource
import com.beank.data.datasource.local.mapper.toRecordModel
import com.beank.data.datasource.local.mapper.toWeekRecord
import com.beank.domain.model.Record
import com.beank.domain.repository.RecordRepository
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

    override fun getRecordSize(): Int = recordDatasource.getRecordSize()

    override fun insertRecord(record: Record) {
        recordDatasource.insertRecord(record.toWeekRecord())
    }

    override fun updateRecord(endTime: LocalDateTime, progressTime: Long, id: Int, pause: Boolean) {
        recordDatasource.updateRecord(endTime, progressTime, id, pause)
    }
}