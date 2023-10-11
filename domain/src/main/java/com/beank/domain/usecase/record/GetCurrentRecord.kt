package com.beank.domain.usecase.record

import com.beank.domain.repository.RecordRepository
import javax.inject.Inject

class GetCurrentRecord @Inject constructor(
    private val recordRepository: RecordRepository
) {
    suspend operator fun invoke() = recordRepository.getCurrentRecord()
}