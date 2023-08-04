package com.example.data.datasource.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.datasource.local.database.entity.WeekRecord
import com.example.data.datasource.local.database.entity.WeekSchedule
import com.example.data.datasource.local.database.entity.WeekTag


@Database(entities = [WeekSchedule::class, WeekTag::class, WeekRecord::class], version = 2, exportSchema = true)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase(){

    abstract fun weekScheduleDao() : WeekSchedule
    abstract fun weekRecordDao() : WeekRecord
    abstract fun weekTagDao() : WeekTag
}