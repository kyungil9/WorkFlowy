package com.beank.domain.usecase.tag

import com.beank.domain.model.Tag
import com.beank.domain.repository.TagRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InsertTag @Inject constructor(
    private val tagRepository: TagRepository
){
    operator fun invoke(tag: Tag) = tagRepository.insertTag(tag)
}