package com.beank.workFlowy.screen.schedule

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Stable
import com.beank.presentation.R
import java.time.LocalDate
import java.time.LocalTime

@Stable
@RequiresApi(Build.VERSION_CODES.O)
data class ScheduleUiState(
    val scheduleImageList : List<Int> = emptyList(),
    val id : String = "",
    val title : String = "",
    val image : Int = R.drawable.baseline_calendar_today_24,
    val date : LocalDate = LocalDate.now(),
    val startTime : LocalTime = LocalTime.now(),
    val endTime : LocalTime = LocalTime.now(),
    val comment : String = "",
    val timeToggle : Boolean = false,
)