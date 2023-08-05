package com.example.domain.usecase.impl

import com.example.domain.model.Tag
import com.example.domain.repository.TagRepository
import com.example.domain.usecase.TagUsecase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TagUsecaseImpl @Inject constructor(
    private val tagRepository: TagRepository
) : TagUsecase {

    override fun getTagInfo(): Flow<List<Tag>> = tagRepository.getTagInfo()

    override fun getTagSingleInfo(): Tag = tagRepository.getTagSingleInfo()

    override fun insertTag(tag: Tag) {
        tagRepository.insertTag(tag)
    }

    override fun deleteTag(tag: Tag) {
        tagRepository.deleteTag(tag)
    }
}