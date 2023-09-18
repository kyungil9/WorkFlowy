package com.beank.domain.usecase.record

import com.beank.domain.repository.RecordRepository
import com.beank.domain.repository.TagRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetNowRecord @Inject constructor(
    private val recordRepository: RecordRepository
){
    operator fun invoke(pause: Boolean) = recordRepository.getPauseRecord(pause)

}