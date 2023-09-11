package com.beank.data.datasource.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.beank.data.datasource.local.database.dao.WeekRecordDao
import com.beank.data.datasource.local.database.dao.WeekScheduleDao
import com.beank.data.datasource.local.database.dao.WeekTagDao
import com.beank.data.datasource.local.database.entity.WeekRecord
import com.beank.data.datasource.local.database.entity.WeekSchedule
import com.beank.data.datasource.local.database.entity.WeekTag


@Database(entities = [WeekSchedule::class, WeekTag::class, WeekRecord::class], version = 5, exportSchema = true)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase(){

    abstract fun weekScheduleDao() : WeekScheduleDao
    abstract fun weekRecordDao() : WeekRecordDao
    abstract fun weekTagDao() : WeekTagDao
}