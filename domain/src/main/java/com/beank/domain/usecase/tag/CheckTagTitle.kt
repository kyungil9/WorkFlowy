package com.beank.domain.usecase.tag

import com.beank.domain.repository.TagRepository
import javax.inject.Inject
import javax.inject.Singleton

class CheckTagTitle (
    private val tagRepository: TagRepository
){
    suspend operator fun invoke(title : String) = tagRepository.checkTagTitle(title)
}