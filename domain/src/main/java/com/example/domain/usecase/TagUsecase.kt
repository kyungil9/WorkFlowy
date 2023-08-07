package com.example.domain.usecase

import com.example.domain.model.Tag
import kotlinx.coroutines.flow.Flow

interface TagUsecase {

    fun getTagInfo() : Flow<List<Tag>>

    fun getTagSingleInfo(tag : String) : Tag

    fun getTagSize() : Int

    fun insertTag(tag: Tag)

    fun deleteTag(tag : Tag)

}