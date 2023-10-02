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
            val schedule = savedStateHandle.get<String>("schedule")?.fromScheduleJson()!!
            uiState = uiState.copy(
                id = schedule.id!!,
                title = schedule.title,
                comment = schedule.comment,
                date = schedule.date,
                startTime = schedule.startTime,
                endTime = schedule.endTime,
                timeToggle = schedule.time,
                image = intToImage(schedule.icon,typedSchedule)
            )
        }else{
            val day = today.toLocalDate()
            //time date 완전히 변경으로 쓸모없는 변수 삭제처리하기
            onDateChange(day)
        }
    }

    fun onDateChange(date: LocalDate){
        uiState = uiState.copy(date = date)
    }
    fun onTimeToggleChange(){
        uiState = uiState.copy(timeToggle = uiState.timeToggle.not())
    }
    fun onTitleChange(text : String){
        uiState = uiState.copy(title = text)
    }

    fun onImageChange(image : Int){
        uiState = uiState.copy(image = image)
    }

    fun onTimeChange(startHour : Int, startMinute : Int, endHour : Int, endMinute : Int) : Boolean{
        if (startHour > endHour || (startHour == endHour && startMinute > endMinute)){
            return false
        }else{
            uiState = uiState.copy(startTime = LocalTime.of(startHour, startMinute), endTime = LocalTime.of(endHour, endMinute))
            return true
        }
    }

    fun onCommentChange(text: String){
        uiState = uiState.copy(comment = text)
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
                comment = uiState.comment
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
                comment = uiState.comment
            ))
        }
    }

}