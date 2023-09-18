package com.beank.domain.usecase.record

import com.beank.domain.repository.RecordRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAllRecord @Inject constructor(
    private val recordRepository: RecordRepository
){
    operator fun invoke() = recordRepository.getRecordInfo()
}