package com.beank.domain.usecase.tag

import com.beank.domain.repository.TagRepository
import javax.inject.Inject

class GetNextTag @Inject constructor(
    private val tagRepository: TagRepository
) {
    suspend operator fun invoke(tag : String) = tagRepository.getNextTag(tag)
}