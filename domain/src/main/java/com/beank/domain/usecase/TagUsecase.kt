package com.beank.domain.usecase

import com.beank.domain.model.Tag
import kotlinx.coroutines.flow.Flow

interface TagUsecase {

    fun getTagInfo() : Flow<List<Tag>>

    fun getTagSingleInfo(tag : String) : Tag

    fun getTagSize() : Int

    fun checkTagTitle(title : String) : Int
    fun insertTag(tag: Tag)

    fun deleteTag(tag : Tag)

}