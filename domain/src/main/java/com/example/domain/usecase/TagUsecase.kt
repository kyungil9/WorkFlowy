package com.example.domain.usecase

import com.example.domain.model.Tag
import kotlinx.coroutines.flow.Flow

interface TagUsecase {

    fun getTagInfo() : Flow<List<Tag>>

    fun getTagSingleInfo() : Tag

    fun insertTag(tag: Tag)

    fun deleteTag(tag : Tag)

}