package com.beank.data.repositoryimpl

import com.beank.data.datasource.local.TagDatasource
import com.beank.data.datasource.local.mapper.toTagModel
import com.beank.data.datasource.local.mapper.toWeekTag
import com.beank.domain.model.Tag
import com.beank.domain.repository.TagRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TagRepositoryImpl @Inject constructor(
    private val tagDatasource: TagDatasource
) : TagRepository {

    override fun getTagInfo(): Flow<List<Tag>> =
        tagDatasource.getTagInfo().map { tagList ->
            tagList.map { tag ->
                tag.toTagModel()
            }
        }

    override fun getTagSingleInfo(tag : String): Tag = tagDatasource.getTagSingleInfo(tag).toTagModel()

    override fun getTagSize(): Int = tagDatasource.getTagSize()

    override fun checkTagTitle(title: String): Int = tagDatasource.checkTagTitle(title)

    override fun insertTag(tag: Tag) {
        tagDatasource.insertTag(tag.toWeekTag())
    }

    override fun deleteTag(tag: Tag) {
        tagDatasource.deleteTag(tag.toWeekTag())
    }
}