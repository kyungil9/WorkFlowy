package com.example.data.datasource.local

import com.example.data.datasource.local.database.entity.WeekTag
import kotlinx.coroutines.flow.Flow

interface TagDatasource {

    fun getTagInfo() : Flow<List<WeekTag>>

    fun getTagSingleInfo() : WeekTag

    fun getTagSize() : Int

    fun insertTag(tag: WeekTag)

    fun deleteTag(tag : WeekTag)
}