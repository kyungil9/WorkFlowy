package com.example.data.datasource.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.data.datasource.local.database.entity.WeekRecord
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface WeekRecordDao {
    @Query("select * from WeekRecord")
    fun getAllRecordInfo(): Flow<List<WeekRecord>>

    @Query("select * from WeekRecord where (startTime >= :startDateTime and endTime < :endDateTime) or (startTime >= :startDateTime and startTime <= :endDateTime) or (:startDateTime <= endTime and endTime <= :endDateTime) or (startTime < :startDateTime  and endTime is Null) or (startTime < :startDateTime and endTime > :endDateTime)")//추후 수정 필요??,하루를 넘기는 데이터를 어찌 처리할지
    fun getTodayRecordInfo(startDateTime: Long ,endDateTime: Long): Flow<List<WeekRecord>>

    @Insert
    fun insertRecord(record : WeekRecord)

    @Query("update WeekRecord set endTime = :mEndTime,progressTime = :mProgressTime, pause = :pause WHERE id = :id")
    fun updateRecord(mEndTime: LocalDateTime, mProgressTime : Long, id : Int, pause : Boolean)

    @Query("select * from WeekRecord where pause = :pause")
    fun getSelectRecordInfo(pause: Boolean): Flow<List<WeekRecord>>

    @Query("select count(*) from WeekRecord")
    fun getRecordSize() : Int
}