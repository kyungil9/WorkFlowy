package com.beank.data.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

import com.beank.data.datasource.local.database.Database
import com.beank.data.datasource.local.database.dao.WeekRecordDao
import com.beank.data.datasource.local.database.dao.WeekScheduleDao
import com.beank.data.datasource.local.database.dao.WeekTagDao

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Singleton
    val MIGRATION_2_3 = object : Migration(4,5){
        override fun migrate(database: SupportSQLiteDatabase) {

        }
    }//

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context) : Database {
        return Room.databaseBuilder(context, Database::class.java,"mySchedule.db").addMigrations(MIGRATION_2_3).build()
    }

    @Provides
    @Singleton
    fun providesWeekScheduleDao(database: Database): WeekScheduleDao {
        return database.weekScheduleDao()
    }

    @Provides
    @Singleton
    fun providesWeekRecordDao(database: Database): WeekRecordDao {
        return database.weekRecordDao()
    }

    @Provides
    @Singleton
    fun providesWeekTagDao(database: Database): WeekTagDao {
        return database.weekTagDao()
    }


}