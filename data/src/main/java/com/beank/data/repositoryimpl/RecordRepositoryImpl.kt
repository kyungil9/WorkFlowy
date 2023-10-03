package com.beank.data.repositoryimpl

import android.os.Build
import androidx.annotation.RequiresApi
import com.beank.data.datasource.StorageDataSource
import com.beank.data.entity.WeekRecord
import com.beank.data.entity.WeekTag
import com.beank.data.mapper.toInt
import com.beank.data.mapper.toLong
import com.beank.data.mapper.toTagModel
import com.beank.data.mapper.toWeekRecord
import com.beank.data.utils.dataStateObjects
import com.beank.domain.model.FireStoreState
import com.beank.domain.model.NowRecord
import com.beank.domain.model.Record
import com.beank.domain.model.onException
import com.beank.domain.model.onLoading
import com.beank.domain.model.onSuccess
import com.beank.domain.repository.RecordRepository
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class RecordRepositoryImpl @Inject constructor(
    private val storage : StorageDataSource
): RecordRepository {

    override fun getRecordInfo(): Flow<FireStoreState<List<Record>>> =
        storage.store.document(storage.getUid()!!).collection(RECORD)
            .dataStateObjects<WeekRecord,Record>()

    override fun getPeriodRecord(startDate: LocalDate, endDate: LocalDate): Flow<FireStoreState<List<Record>>> =
        storage.store.document(storage.getUid()!!).collection(RECORD).whereLessThanOrEqualTo("date",endDate.toInt())
            .whereGreaterThanOrEqualTo("date",startDate.toInt()).dataStateObjects<WeekRecord,Record>()

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
    }

    override fun insertRecord(record: Record) : Unit =
        storage.save(RECORD,record.toWeekRecord())

    override fun updateRecord(id: String, endTime: LocalDateTime, progressTime: Long, pause: Boolean) {
        storage.update(RECORD,id, mapOf(
            "endTime" to endTime.toLong(),
            "progressTime" to progressTime,
            "pause" to pause
        ))
    }

    override fun updateRecord(id: String, endTime: LocalDateTime, progressTime: Long, pause: Boolean, date: LocalDate) {
        storage.update(RECORD,id, mapOf(
            "endTime" to endTime.toLong(),
            "progressTime" to progressTime,
            "pause" to pause,
            "date" to date.toInt()
        ))
    }

    companion object {
        private const val RECORD = "Record"
    }

}