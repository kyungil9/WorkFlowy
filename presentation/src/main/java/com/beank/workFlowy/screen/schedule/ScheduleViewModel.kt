package com.beank.workFlowy.screen.schedule

import android.content.res.TypedArray
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.chargemap.compose.numberpicker.FullHours
import com.chargemap.compose.numberpicker.Hours
import com.beank.domain.model.Schedule
import com.beank.domain.repository.LogRepository
import com.beank.domain.usecase.ScheduleUsecases
import com.beank.presentation.R
import com.beank.workFlowy.screen.WorkFlowyViewModel
import com.beank.workFlowy.utils.changeDayInfo
import com.beank.workFlowy.utils.fromScheduleJson
import com.beank.workFlowy.utils.imageToInt
import com.beank.workFlowy.utils.intToImage
import com.beank.workFlowy.utils.toLocalDate
import com.beank.workFlowy.utils.transDayToShortKorean
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val savedStateHandle : SavedStateHandle,
    private val scheduleUsecases: ScheduleUsecases,
    logRepository: LogRepository
) : WorkFlowyViewModel(logRepository){

    private lateinit var typedSchedule : TypedArray
    var uiState by mutableStateOf(ScheduleUiState())
        private set

    fun initScheduleImages(scheduleList : TypedArray){
        typedSchedule = scheduleList
        launchCatching {
            val scheduleImages = ArrayList<Int>()
            for (i in 0 until scheduleList.length()){
                scheduleImages.add(scheduleList.getResourceId(i,0))
            }
            viewModelScope.launch(Dispatchers.Main) {
                uiState = uiState.copy(scheduleImageList = scheduleImages)
            }
        }

        val today = savedStateHandle.get<Int>("today")!!
        if (today == 0){
            val schedule = savedStateHandle.get<String>("schedule")?.let {json ->
                json.fromScheduleJson()
            }!!
            uiState = uiState.copy(
                id = schedule.id!!,
                title = schedule.title,
                comment = schedule.comment,
                year = schedule.date.year,
                month = schedule.date.monthValue,
                day = schedule.date.dayOfMonth,
                startTime = FullHours(schedule.startTime.hour,schedule.startTime.minute),
                endTime = FullHours(schedule.endTime.hour,schedule.endTime.minute),
                timeToggle = schedule.time,
                image = intToImage(schedule.icon,typedSchedule)
            )
        }else{
            val day = today.toLocalDate()
            uiState = uiState.copy(
                year = day.year,
                month = day.monthValue,
                day = day.dayOfMonth
            )
        }
    }

    fun onImageToggleChange(){
        uiState = uiState.copy(imageToggle = uiState.imageToggle.not())
    }
    fun onDateToggleChange(){
        uiState = uiState.copy(dateToggle = uiState.dateToggle.not())
    }
    fun onDateToggleChange(value : Boolean){
        uiState = uiState.copy(dateToggle = value)
    }
    fun onTimeToggleChange(){
        uiState = uiState.copy(timeToggle = uiState.timeToggle.not())
    }
    fun onEndTimeToggleChange(value: Boolean){
        uiState = uiState.copy(endTimeToggle = value)
    }

    fun onTitleChange(text : String){
        uiState = uiState.copy(title = text)
    }

    fun onImageChange(image : Int){
        uiState = uiState.copy(image = image)
    }

    fun onYearChange(year : Int){
        uiState = uiState.copy(
            year = year,
            day = 1,
            endDayofMonth = changeDayInfo(LocalDate.of(uiState.year,uiState.month,1))
        )
    }

    fun onMonthChange(month : Int){
        uiState = uiState.copy(
            month = month,
            day = 1,
            endDayofMonth = changeDayInfo(LocalDate.of(uiState.year,uiState.month,1))
        )
    }

    fun onDayChange(day : Int){
        uiState = uiState.copy(day = day)
    }

    fun onStartTimeChange(time : Hours){
        uiState = uiState.copy(startTime = FullHours(time.hours,time.minutes))
    }

    fun onEndTimeChange(time : Hours){
        uiState = uiState.copy(endTime = FullHours(time.hours,time.minutes))
    }

    fun selectDayOfWeek(year: Int,month: Int,day: Int) : String{
        return transDayToShortKorean(LocalDate.of(year,month,day).dayOfWeek.value)
    }

    fun onCommentChange(text: String){
        uiState = uiState.copy(comment = text)
    }

    fun onScheduleInsert(){
        launchCatching {
            scheduleUsecases.insertSchedule(Schedule(
                id = null,
                date = LocalDate.of(uiState.year,uiState.month,uiState.day),
                startTime = LocalTime.of(uiState.startTime.hours,uiState.startTime.minutes,0),
                endTime = LocalTime.of(uiState.endTime.hours,uiState.endTime.minutes,0),
                time = uiState.timeToggle,
                icon = imageToInt(uiState.image,typedSchedule),
                title = uiState.title,
                comment = uiState.comment
            ))
        }
    }

    fun onScheduleUpdate(){
        launchCatching {
            scheduleUsecases.updateSchedule(Schedule(
                id = uiState.id,
                date = LocalDate.of(uiState.year,uiState.month,uiState.day),
                startTime = LocalTime.of(uiState.startTime.hours,uiState.startTime.minutes,0),
                endTime = LocalTime.of(uiState.endTime.hours,uiState.endTime.minutes,0),
                time = uiState.timeToggle,
                icon = imageToInt(uiState.image,typedSchedule),
                title = uiState.title,
                comment = uiState.comment
            ))
        }
    }

}