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

    @Query("select * from WeekTag where title = :tag limit 1")
    fun getToDoOneTag(tag: String) : WeekTag

    @Query("select count(*) from WeekTag")
    fun getTagSize() : Int

    @Query("select count(*) from WeekTag where title = :title limit 1")
    fun checkTagTitle(title : String) : Int

    @Insert
    fun insertTag(tag : WeekTag)

    @Query("Delete from WeekTag where WeekTag.title = :tag")
    fun deleteTag(tag: String)


}