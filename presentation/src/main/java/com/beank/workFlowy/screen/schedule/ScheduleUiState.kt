package com.beank.workFlowy.screen.schedule

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.beank.domain.model.Schedule
import com.beank.presentation.R
import java.time.LocalDate
import java.time.LocalTime

@Stable
@RequiresApi(Build.VERSION_CODES.O)
class ScheduleUiState(
    scheduleImageList : List<Int> = emptyList(),
    id : String = "",
    title : String = "",
    image : Int = R.drawable.baseline_calendar_today_24,
    date : LocalDate = LocalDate.now(),
    startTime : LocalTime = LocalTime.now(),
    endTime : LocalTime = LocalTime.now(),
    comment : String = "",
    timeToggle : Boolean = false,
    alarmToggle :Boolean = false,
    alarmState : String = "5분전"
){
    var scheduleImageList by mutableStateOf(scheduleImageList)
    var id by mutableStateOf(id)
    var title by mutableStateOf(title)
    var image by mutableStateOf(image)
    var date by mutableStateOf(date)
    var startTime by mutableStateOf(startTime)
    var endTime by mutableStateOf(endTime)
    var comment by mutableStateOf(comment)
    var timeToggle by mutableStateOf(timeToggle)
    var alarmToggle by mutableStateOf(alarmToggle)
    var alarmState by mutableStateOf(alarmState)
    var originalSchedule = Schedule()
}