package com.beank.workFlowy.screen.trigger_setting

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.beank.domain.model.GeofenceEvent
import com.beank.domain.model.Tag
import java.time.LocalTime
@RequiresApi(Build.VERSION_CODES.O)
class TriggerSettingUiState(
    tagList : List<Tag> = emptyList(),
    id : String = "",
    tag : String = "",
    tagImage : Int = 0,
    latitude : Double = 37.5642135,
    lonitude : Double = 127.0016985,
    radius : Double = 30.0,
    delayTime : Int = 300000,
    timeOption : Boolean = false,
    startTime : LocalTime = LocalTime.now(),
    endTime : LocalTime = LocalTime.now(),
    geoEvent : Int = GeofenceEvent.EnterRequest
) {
    var tagList by mutableStateOf(tagList)
    var id by mutableStateOf(id)
    var tag by mutableStateOf(tag)
    var tagImage by mutableIntStateOf(tagImage)
    var latitude by mutableDoubleStateOf(latitude)
    var lonitude by mutableDoubleStateOf(lonitude)
    var radius by mutableDoubleStateOf(radius)
    var delayTime by mutableIntStateOf(delayTime)
    var timeOption by mutableStateOf(timeOption)
    var startTime by mutableStateOf(startTime)
    var endTime by mutableStateOf(endTime)
    var geoEvent by mutableIntStateOf(geoEvent)
}