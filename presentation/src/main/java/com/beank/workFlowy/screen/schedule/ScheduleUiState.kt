package com.beank.workFlowy.screen.schedule

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Stable
import com.beank.presentation.R
import com.beank.workFlowy.utils.changeDayInfo
import com.chargemap.compose.numberpicker.FullHours
import java.time.LocalDate
import java.time.LocalTime

@Stable
@RequiresApi(Build.VERSION_CODES.O)
data class ScheduleUiState(
    val scheduleImageList : List<Int> = emptyList(),
    val id : String = "",
    val title : String = "",
    val image : Int = R.drawable.baseline_calendar_today_24,
    val year : Int = LocalDate.now().year,
    val month : Int = LocalDate.now().monthValue,
    val day : Int = LocalDate.now().dayOfMonth,
    val startTime : FullHours = FullHours(LocalTime.now().hour,LocalTime.now().minute),
    val endTime :FullHours = FullHours(LocalTime.now().hour,LocalTime.now().minute),
    val endDayofMonth : Int = changeDayInfo(LocalDate.now()),
    val comment : String = "",
    val imageToggle : Boolean = false,
    val dateToggle : Boolean = false,
    val timeToggle : Boolean = false,
    val endTimeToggle : Boolean = false
)