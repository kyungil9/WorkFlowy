package com.example.domain.repository

import com.example.domain.model.Tag
import kotlinx.coroutines.flow.Flow

interface TagRepository {

    fun getTagInfo() : Flow<List<Tag>>

    fun getTagSingleInfo() : Tag

    fun getTagSize() : Int

    fun insertTag(tag: Tag)

    fun deleteTag(tag : Tag)
}