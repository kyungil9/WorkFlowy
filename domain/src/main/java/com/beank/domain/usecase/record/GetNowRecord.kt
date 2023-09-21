package com.beank.domain.usecase.record

import com.beank.domain.repository.RecordRepository
import com.beank.domain.repository.TagRepository
import javax.inject.Inject
import javax.inject.Singleton

class GetNowRecord(
    private val recordRepository: RecordRepository
){
    operator fun invoke(pause: Boolean) = recordRepository.getPauseRecord(pause)

}