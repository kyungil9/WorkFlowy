package com.beank.domain.usecase.tag

import com.beank.domain.repository.TagRepository
import javax.inject.Inject
import javax.inject.Singleton

class GetAllTag(
    private val tagRepository: TagRepository
){
    operator fun invoke() = tagRepository.getTagInfo()
}