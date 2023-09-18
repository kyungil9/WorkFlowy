package com.beank.domain.repository

import com.beank.domain.model.Tag
import kotlinx.coroutines.flow.Flow

interface TagRepository {

    fun getTagInfo() : Flow<List<Tag>>

    suspend fun checkTagTitle(title : String) : Boolean

    fun insertTag(tag: Tag)

    fun deleteTag(tag : Tag)
}