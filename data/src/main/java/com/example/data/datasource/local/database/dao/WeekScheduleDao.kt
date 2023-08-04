package com.example.data.datasource.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.data.WeekSchedule
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface WeekScheduleDao {
    @Query("select * from WeekSchedule where date = :today order by startTime asc")
    fun getSearchSchedule(today : LocalDate) : Flow<List<com.example.data.WeekSchedule>>

    @Insert
    fun insertSchedule(schedule : com.example.data.WeekSchedule)

    @Update
    fun updateSchedule(schedule: com.example.data.WeekSchedule)

    @Delete
    fun deleteSchedule(schedule : com.example.data.WeekSchedule)
}