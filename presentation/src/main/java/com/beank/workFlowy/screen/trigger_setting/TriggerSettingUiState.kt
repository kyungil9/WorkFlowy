package com.beank.workFlowy.screen.trigger_setting

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.beank.domain.model.GeofenceEvent
import com.beank.domain.model.Tag
import java.time.LocalTime
@RequiresApi(Build.VERSION_CODES.O)
class TriggerSettingUiState(
    tagList : List<Tag> = emptyList(),
    id : String = "",
    enterTag : String = "휴식중",
    exitTag : String = "휴식중",
    enterTagImage : Int = 2,
    exitTagImage : Int = 2,
    latitude : Double = 37.5642135,
    lonitude : Double = 127.0016985,
    radius : Double = 100.0,
    delayTime : Int = 300000,
    timeOption : Boolean = false,
    startTime : LocalTime = LocalTime.now(),
    endTime : LocalTime = LocalTime.now(),
    geoEvent : Int = GeofenceEvent.EnterRequest
) {
    var tagList by mutableStateOf(tagList)
    var id by mutableStateOf(id)
    var enterTag by mutableStateOf(enterTag)
    var exitTag by mutableStateOf(exitTag)
    var enterTagImage by mutableIntStateOf(enterTagImage)
    var exitTagImage by mutableIntStateOf(exitTagImage)
    var latitude by mutableDoubleStateOf(latitude)
    var lonitude by mutableDoubleStateOf(lonitude)
    var radius by mutableDoubleStateOf(radius)
    var delayTime by mutableIntStateOf(delayTime)
    var timeOption by mutableStateOf(timeOption)
    var startTime by mutableStateOf(startTime)
    var endTime by mutableStateOf(endTime)
    var geoEvent by mutableIntStateOf(geoEvent)
}