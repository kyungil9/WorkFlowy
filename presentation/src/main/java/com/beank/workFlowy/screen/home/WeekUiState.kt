package com.beank.workFlowy.screen.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Stable
import com.beank.domain.model.Record
import com.beank.domain.model.Schedule
import com.beank.domain.model.Tag
import java.time.Duration
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Stable
@RequiresApi(Build.VERSION_CODES.O)
data class WeekUiState(
    val tagList : List<Tag> = emptyList(),
    val scheduleList : List<Schedule> = emptyList(),
    val recordList : List<Record> = emptyList(),
    val selectTag : Tag = Tag(),
    val selectSchedule : Schedule = Schedule(),
    val actProgress : Boolean = true,
    val weekState : Boolean = false,
    val scheduleState : Boolean = false,
    val listCenter : Int = ChronoUnit.DAYS.between(LocalDate.of(2021,12,28), LocalDate.now()).toInt() -3
)