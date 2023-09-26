package com.beank.workFlowy.screen.analysis

import androidx.compose.runtime.Stable
import com.beank.domain.model.Record

@Stable
data class AnalysisUiState(
    val recordList : List<Record> = emptyList(),
    val actProgress : Boolean = true
)