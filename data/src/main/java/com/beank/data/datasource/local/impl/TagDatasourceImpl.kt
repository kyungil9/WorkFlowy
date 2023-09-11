package com.beank.data.datasource.local.impl

import com.beank.data.datasource.local.TagDatasource
import com.beank.data.datasource.local.database.dao.WeekTagDao
import com.beank.data.datasource.local.database.entity.WeekTag
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TagDatasourceImpl @Inject constructor(
    private val tagService : WeekTagDao
) : TagDatasource {

    override fun getTagInfo(): Flow<List<WeekTag>> = tagService.getAllTag()

    override fun getTagSingleInfo(tag : String): WeekTag = tagService.getToDoOneTag(tag)

    override fun getTagSize(): Int = tagService.getTagSize()

    override fun checkTagTitle(title : String) : Int = tagService.checkTagTitle(title)

    override fun insertTag(tag: WeekTag) {
        tagService.insertTag(tag)
    }

    override fun deleteTag(tag: WeekTag) {
        tagService.deleteTag(tag.title)
    }
}