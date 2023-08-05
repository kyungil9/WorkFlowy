package com.example.data.datasource.local.impl

import com.example.data.datasource.local.TagDatasource
import com.example.data.datasource.local.database.dao.WeekTagDao
import com.example.data.datasource.local.database.entity.WeekTag
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TagDatasourceImpl @Inject constructor(
    private val tagService : WeekTagDao
) : TagDatasource {

    override fun getTagInfo(): Flow<List<WeekTag>> = tagService.getAllTag()

    override fun getTagSingleInfo(): WeekTag = tagService.getToDoOneTag()

    override fun insertTag(tag: WeekTag) {
        tagService.insertTag(tag)
    }

    override fun deleteTag(tag: WeekTag) {
        tagService.deleteTag(tag.title)
    }
}