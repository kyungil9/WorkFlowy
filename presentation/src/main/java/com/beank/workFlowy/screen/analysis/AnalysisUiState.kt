package com.beank.workFlowy.screen.analysis

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.beank.domain.model.Record

@Stable
class AnalysisUiState(
    recordList : List<Record> = emptyList(),
    actProgress : Boolean = true
){
    var recordList by mutableStateOf(recordList)
    var actProgress by mutableStateOf(actProgress)
}