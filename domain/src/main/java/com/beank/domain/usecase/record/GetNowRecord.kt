package com.beank.domain.usecase.record

import com.beank.domain.repository.RecordRepository
import javax.inject.Inject

class GetNowRecord @Inject constructor(
    private val recordRepository: RecordRepository
){
    suspend operator fun invoke(pause: Boolean) = recordRepository.getPauseRecord(pause)

    suspend operator fun invoke() = recordRepository.getNowRecord()

}