package com.beank.data.repositoryimpl

import com.beank.data.datasource.StorageDataSource
import com.beank.data.entity.WeekRecord
import com.beank.data.entity.WeekTag
import com.beank.data.mapper.localDateTimeToLong
import com.beank.data.mapper.toRecordModel
import com.beank.data.mapper.toTagModel
import com.beank.data.mapper.toWeekRecord
import com.beank.data.utils.dataStateObjects
import com.beank.domain.model.FireStoreState
import com.beank.domain.model.NowRecord
import com.beank.domain.model.Record
import com.beank.domain.model.Tag
import com.beank.domain.model.onEmpty
import com.beank.domain.model.onException
import com.beank.domain.model.onLoading
import com.beank.domain.model.onSuccess
import com.beank.domain.repository.RecordRepository
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import javax.inject.Inject

class RecordRepositoryImpl @Inject constructor(
    private val storage : StorageDataSource
): RecordRepository {

    override fun getRecordInfo(): Flow<FireStoreState<List<Record>>> =
        storage.store.document(storage.getUid()!!).collection(RECORD)
            .dataStateObjects<WeekRecord,Record>().flowOn(Dispatchers.IO)


    override fun getTodayRecord(startDateTime: Long, endDateTime: Long): Flow<FireStoreState<List<Record>>> =
        storage.store.document(storage.getUid()!!).collection(RECORD).where(Filter.or(
            Filter.and(
                Filter.greaterThanOrEqualTo("startTime",startDateTime),
                Filter.lessThanOrEqualTo("endTime",endDateTime)
            ),
            Filter.and(
                Filter.greaterThanOrEqualTo("startTime",startDateTime),
                Filter.lessThanOrEqualTo("startTime",endDateTime)
            ),
            Filter.and(
                Filter.greaterThanOrEqualTo("endTime",startDateTime),
                Filter.lessThanOrEqualTo("endTime",endDateTime)
            ),
            Filter.and(
                Filter.lessThan("startTime",startDateTime),
                Filter.equalTo("endTime",null)
            ),
            Filter.and(
                Filter.lessThan("startTime",startDateTime),
                Filter.greaterThan("endTime",endDateTime)
            )
        )).dataStateObjects<WeekRecord,Record>().flowOn(Dispatchers.IO)

    override fun getPauseRecord(pause: Boolean): Flow<FireStoreState<NowRecord>> = channelFlow {
        send(FireStoreState.Loading)
        storage.store.document(storage.getUid()!!).collection(RECORD).whereEqualTo("pause",pause)
            .dataStateObjects<WeekRecord,Record>().collectLatest {state ->
                state.onSuccess {record ->
                    val tag = storage.store.document(storage.getUid()!!).collection(TagRepositoryImpl.TAG)
                        .whereEqualTo("title", record[0].tag).get()
                        .await().documents[0].toObject<WeekTag>()!!.toTagModel()
                    send(FireStoreState.Success(NowRecord(record[0],tag)))
                }
                state.onLoading {
                    send(FireStoreState.Loading)
                }
                state.onException { message, e ->
                    send(FireStoreState.Exception(message, e))
                }
            }
    }.flowOn(Dispatchers.IO)

    override fun insertRecord(record: Record) : Unit =
        storage.save(RECORD,record.toWeekRecord())


    override fun updateRecord(id: String, endTime: LocalDateTime, progressTime: Long, pause: Boolean) {
        storage.update(RECORD,id, mapOf(
            "endTime" to endTime.localDateTimeToLong(),
            "progressTime" to progressTime,
            "pause" to pause
        ))
    }

    companion object {
        private const val RECORD = "Record"
    }

}