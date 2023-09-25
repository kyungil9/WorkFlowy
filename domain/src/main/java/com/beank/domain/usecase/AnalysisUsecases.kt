package com.beank.domain.usecase

import com.beank.domain.usecase.record.GetTodayRecord

data class AnalysisUsecases(
    val getTodayRecord: GetTodayRecord
)