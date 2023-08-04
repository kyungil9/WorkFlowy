package com.example.data.datasource.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.data.datasource.local.database.entity.WeekTag
import kotlinx.coroutines.flow.Flow

@Dao
interface WeekTagDao {
    @Query("select * from WeekTag ")
    fun getAllTag(): Flow<List<WeekTag>>

    @Query("select * from WeekTag limit 1")
    fun getToDoOneTag() : WeekTag

    @Insert
    fun insertTag(tag : WeekTag)

    @Query("Delete from WeekTag where WeekTag.title = :tag")
    fun deleteTag(tag: String)
}