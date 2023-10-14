package com.beank.workFlowy.screen.trigger

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.beank.domain.model.GeofenceData

class TriggerUiState(
    triggerList : List<GeofenceData> = emptyList(),
    progressToggle : Boolean = true
) {
    var triggerList by mutableStateOf(triggerList)
    var progressToggle by mutableStateOf(progressToggle)
}