package com.beank.data.datasource.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.beank.data.datasource.local.database.entity.WeekSchedule
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface WeekScheduleDao {
    @Query("select * from WeekSchedule where date = :today order by startTime asc")
    fun getSearchSchedule(today : LocalDate) : Flow<List<WeekSchedule>>

    @Insert
    fun insertSchedule(schedule : WeekSchedule)

    @Update
    fun updateSchedule(schedule: WeekSchedule)

    @Delete
    fun deleteSchedule(schedule : WeekSchedule)
}