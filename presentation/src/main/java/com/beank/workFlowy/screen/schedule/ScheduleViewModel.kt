package com.beank.workFlowy.screen.schedule

import android.content.res.TypedArray
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.beank.domain.model.Schedule
import com.beank.domain.repository.LogRepository
import com.beank.domain.usecase.ScheduleUsecases
import com.beank.workFlowy.screen.WorkFlowyViewModel
import com.beank.workFlowy.utils.fromScheduleJson
import com.beank.workFlowy.utils.imageToInt
import com.beank.workFlowy.utils.intToImage
import com.beank.workFlowy.utils.toLocalDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
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

    val uiState = ScheduleUiState()

    fun initScheduleImages(scheduleList : TypedArray){
        typedSchedule = scheduleList
        launchCatching {
            val scheduleImages = ArrayList<Int>()
            for (i in 0 until scheduleList.length()){
                scheduleImages.add(scheduleList.getResourceId(i,0))
            }
            viewModelScope.launch(Dispatchers.Main) {
                uiState.scheduleImageList = scheduleImages
            }
        }

        val today = savedStateHandle.get<Int>("today")!!
        if (today == 0){
            val schedule = savedStateHandle.get<String>("schedule")?.fromScheduleJson()!!
            uiState.id = schedule.id!!
            uiState.title = schedule.title
            uiState.comment = schedule.comment
            uiState.date = schedule.date
            uiState.startTime = schedule.startTime
            uiState.endTime = schedule.endTime
            uiState.timeToggle = schedule.time
            uiState.image = intToImage(schedule.icon,typedSchedule)
            uiState.alarmToggle = schedule.alarm
            uiState.alarmState = schedule.alarmState
        }else{
            val day = today.toLocalDate()
            onDateChange(day)
        }
    }

    fun onDateChange(date: LocalDate){
        uiState.date = date
    }
    fun onTimeToggleChange(){
        uiState.timeToggle = uiState.timeToggle.not()
    }
    fun onTitleChange(text : String){
        uiState.title = text
    }

    fun onImageChange(image : Int){
        uiState.image = image
    }

    fun onTimeChange(startHour : Int, startMinute : Int, endHour : Int, endMinute : Int) : Boolean{
        return if (startHour > endHour || (startHour == endHour && startMinute > endMinute)){
            false
        }else{
            uiState.startTime = LocalTime.of(startHour, startMinute)
            uiState.endTime = LocalTime.of(endHour, endMinute)
            true
        }
    }

    fun onCommentChange(text: String){
        uiState.comment = text
    }

    fun onAlarmToggleChange(value : Boolean){
        uiState.alarmToggle = value
    }

    fun onAlarmStateChange(state : String){
        uiState.alarmState = state
    }

    private fun onAlarmTimeCalculate() : LocalDateTime{
        val alarmTime = LocalDateTime.of(uiState.date,uiState.startTime)
        return when(uiState.alarmState){
            "5분전" -> alarmTime.minusMinutes(5)
            "30분전" -> alarmTime.minusMinutes(30)
            "1시간전" -> alarmTime.minusHours(1)
            "3시간전" -> alarmTime.minusHours(3)
            "6시간전" -> alarmTime.minusHours(6)
            "12시간전" -> alarmTime.minusHours(12)
            "하루전" -> alarmTime.minusDays(1)
            else -> alarmTime
        }
    }

    fun onScheduleInsert(){
        launchCatching {
            scheduleUsecases.insertSchedule(Schedule(
                id = null,
                date = uiState.date,
                startTime = uiState.startTime,
                endTime = uiState.endTime,
                time = uiState.timeToggle,
                icon = imageToInt(uiState.image,typedSchedule),
                title = uiState.title,
                comment = uiState.comment,
                alarm = uiState.alarmToggle,
                alarmTime = onAlarmTimeCalculate(),
                alarmState = uiState.alarmState
            ))
        }
    }

    fun onScheduleUpdate(){
        launchCatching {
            scheduleUsecases.updateSchedule(Schedule(
                id = uiState.id,
                date = uiState.date,
                startTime = uiState.startTime,
                endTime = uiState.endTime,
                time = uiState.timeToggle,
                icon = imageToInt(uiState.image,typedSchedule),
                title = uiState.title,
                comment = uiState.comment,
                alarm = uiState.alarmToggle,
                alarmTime = onAlarmTimeCalculate(),
                alarmState = uiState.alarmState
            ))
        }
    }

}