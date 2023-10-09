package com.beank.workFlowy.screen.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.beank.domain.model.Record
import com.beank.domain.model.Schedule
import com.beank.domain.model.Tag
import com.beank.workFlowy.utils.Date
import com.beank.workFlowy.utils.makeDayList
import java.time.Duration
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Stable
@RequiresApi(Build.VERSION_CODES.O)
class WeekUiState(
    tagList : List<Tag> = emptyList(),
    scheduleList : List<Schedule> = emptyList(),
    recordList : List<Record> = emptyList(),
    selectTag : Tag = Tag(),
    selectSchedule : Schedule = Schedule(),
    actProgress : Boolean = true,
    weekState : Boolean = false,
    scheduleState : Boolean = false,
    listCenter : Int = ChronoUnit.DAYS.between(LocalDate.of(2021,12,28), LocalDate.now()).toInt() -3,
    duration: Duration = Duration.ZERO,
    weekDayList: List<Date> = makeDayList()
){
    var tagList by mutableStateOf(tagList)
    var scheduleList by mutableStateOf(scheduleList)
    var recordList by mutableStateOf(recordList)
    var selectTag by mutableStateOf(selectTag)
    var selectSchedule by mutableStateOf(selectSchedule)
    var actProgress by mutableStateOf(actProgress)
    var weekState by mutableStateOf(weekState)
    var scheduleState by mutableStateOf(scheduleState)
    var listCenter by mutableIntStateOf(listCenter)
    var progressTime by mutableStateOf(duration)
    var weekDayList = mutableStateListOf<Date>().apply { addAll(weekDayList) }

}