package com.example.data.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.R

import com.example.data.datasource.local.database.Database
import com.example.data.datasource.local.database.dao.WeekRecordDao
import com.example.data.datasource.local.database.dao.WeekScheduleDao
import com.example.data.datasource.local.database.dao.WeekTagDao

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context) : Database {
        return Room.databaseBuilder(context, Database::class.java,"mySchedule.db").addMigrations(
            MIGRATION_1_2
        ).build()
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

    @Singleton
    val MIGRATION_1_2 = object : Migration(1,2){
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("insert into WeekTag values (null,${R.drawable.baseline_menu_book_24},'공부중'")
            database.execSQL("insert into WeekTag values (null,${R.drawable.baseline_directions_run_24},'운동중'")
            database.execSQL("insert into WeekTag values (null,${R.drawable.baseline_bed_24},'휴식중'")
            database.execSQL("insert into WeekTag values (null,${R.drawable.baseline_directions_bus_24},'이동중'")
            database.execSQL("insert into WeekTag values (null,${R.drawable.baseline_hotel_24},'수면중'")
            database.execSQL("insert into WeekTag values (null,${R.drawable.baseline_local_cafe_24},'개인시간'")
        }
    }
}