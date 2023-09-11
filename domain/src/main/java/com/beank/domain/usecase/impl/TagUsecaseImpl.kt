package com.beank.domain.usecase.impl

import com.beank.domain.model.Tag
import com.beank.domain.repository.TagRepository
import com.beank.domain.usecase.TagUsecase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TagUsecaseImpl @Inject constructor(
    private val tagRepository: TagRepository
) : TagUsecase {

    override fun getTagInfo(): Flow<List<Tag>> = tagRepository.getTagInfo()

    override fun getTagSingleInfo(tag : String): Tag = tagRepository.getTagSingleInfo(tag)

    override fun getTagSize(): Int = tagRepository.getTagSize()

    override fun checkTagTitle(title: String): Int = tagRepository.checkTagTitle(title)

    override fun insertTag(tag: Tag) {
        tagRepository.insertTag(tag)
    }

    override fun deleteTag(tag: Tag) {
        tagRepository.deleteTag(tag)
    }
}