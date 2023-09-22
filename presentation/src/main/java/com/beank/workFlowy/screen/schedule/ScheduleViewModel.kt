package com.beank.workFlowy.screen.schedule

import android.content.res.TypedArray
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.chargemap.compose.numberpicker.FullHours
import com.chargemap.compose.numberpicker.Hours
import com.beank.domain.model.Schedule
import com.beank.domain.repository.LogRepository
import com.beank.domain.usecase.ScheduleUsecases
import com.beank.workFlowy.R
import com.beank.workFlowy.screen.WorkFlowyViewModel
import com.beank.workFlowy.utils.changeDayInfo
import com.beank.workFlowy.utils.fromScheduleJson
import com.beank.workFlowy.utils.imageToInt
import com.beank.workFlowy.utils.intToImage
import com.beank.workFlowy.utils.toLocalDate
import com.beank.workFlowy.utils.transDayToShortKorean
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@Stable
data class ScheduleUiState(
    val scheduleImageList : List<Int> = emptyList()
)

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val savedStateHandle : SavedStateHandle,
    private val scheduleUsecases: ScheduleUsecases,
    logRepository: LogRepository
) : WorkFlowyViewModel(logRepository){

    private lateinit var typedSchedule : TypedArray
    private val date = LocalDate.now()
    private val time = LocalTime.now()
    var updateId = ""
        private set
    var scheduleUiState by mutableStateOf(ScheduleUiState())
        private set
    var inputScheduleText by mutableStateOf("")
        private set
    var selectScheduleImage by mutableStateOf(R.drawable.baseline_calendar_today_24)
        private set
    var selectPickerYear by mutableStateOf(date.year)
        private set
    var selectPickerMonth by mutableStateOf(date.monthValue)
        private set
    var selectPickerDay by mutableStateOf(date.dayOfMonth)
    var selectPickerStartTime by mutableStateOf(FullHours(time.hour,time.minute))
        private set
    var selectPickerEndTime by mutableStateOf(FullHours(time.hour,time.minute))
        private set
    var endMonthDay by mutableStateOf(changeDayInfo(date))
        private set
    var inputCommentText by mutableStateOf("")
        private set
    var checkImage by mutableStateOf(false)
        private set
    var checkDate by mutableStateOf(false)
        private set
    var checkTime by mutableStateOf(false)
        private set
    var checkendTime by mutableStateOf(false)
        private set


    fun initScheduleImages(scheduleList : TypedArray){
        typedSchedule = scheduleList
        launchCatching {
            val scheduleImages = ArrayList<Int>()
            for (i in 0 until scheduleList.length()){
                scheduleImages.add(scheduleList.getResourceId(i,0))
            }
            viewModelScope.launch(Dispatchers.Main) {
                scheduleUiState = scheduleUiState.copy(scheduleImageList = scheduleImages)
            }
        }

        val today = savedStateHandle.get<Int>("today")!!
        if (today == 0){
            val schedule = savedStateHandle.get<String>("schedule")?.let {json ->
                json.fromScheduleJson()
            }!!
            updateId = schedule.id!!
            inputScheduleText = schedule.title
            inputCommentText = schedule.comment
            selectPickerYear = schedule.date.year
            selectPickerMonth = schedule.date.monthValue
            selectPickerDay = schedule.date.dayOfMonth
            selectPickerStartTime = FullHours(schedule.startTime.hour,schedule.startTime.minute)
            selectPickerEndTime = FullHours(schedule.endTime.hour,schedule.endTime.minute)
            selectScheduleImage = intToImage(schedule.icon,typedSchedule)
        }else{
            val day = today.toLocalDate()
            selectPickerYear = day.year
            selectPickerMonth = day.monthValue
            selectPickerDay= day.dayOfMonth
        }
    }

    fun updateCheckImage(){
        checkImage = checkImage.not()
    }
    fun updateCheckDate(){
        checkDate = checkDate.not()
    }

    fun updateCheckDate(value : Boolean){
        checkDate = value
    }

    fun updateCheckTime(){
        checkTime = checkTime.not()
    }

    fun updateCheckEndTime(value: Boolean){
        checkendTime = value
    }

    fun updateScheduleText(text : String){
        inputScheduleText = text
    }

    fun updateScheduleImage(image : Int){
        selectScheduleImage = image
    }

    fun updateSelectYear(year : Int){
        selectPickerDay = 1//임시방편
        selectPickerYear = year
        endMonthDay = changeDayInfo(LocalDate.of(selectPickerYear,selectPickerMonth,1))
    }

    fun updateSelectMonth(month : Int){
        selectPickerDay = 1
        selectPickerMonth = month
        endMonthDay = changeDayInfo(LocalDate.of(selectPickerYear,selectPickerMonth,1))
    }

    fun updateSelectDay(day : Int){
        selectPickerDay = day
    }

    fun updateSelectStartTime(time : Hours){
        selectPickerStartTime = FullHours(time.hours,time.minutes)
    }

    fun updateSelectEndTime(time : Hours){
        selectPickerEndTime = FullHours(time.hours,time.minutes)
    }

    fun selectDayOfWeek(year: Int,month: Int,day: Int) : String{
        return transDayToShortKorean(LocalDate.of(year,month,day).dayOfWeek.value)
    }

    fun updateCommentText(text: String){
        inputCommentText = text
    }

    fun insertScheduleInfo(){
        launchCatching {
            scheduleUsecases.insertSchedule(Schedule(
                id = null,
                date = LocalDate.of(selectPickerYear,selectPickerMonth,selectPickerDay),
                startTime = LocalTime.of(selectPickerStartTime.hours,selectPickerStartTime.minutes,0),
                endTime = LocalTime.of(selectPickerEndTime.hours,selectPickerEndTime.minutes,0),
                icon = imageToInt(selectScheduleImage,typedSchedule),
                title = inputScheduleText,
                comment = inputCommentText
            ))
        }
    }

    fun updateScheduleInfo(){
        launchCatching {
            scheduleUsecases.updateSchedule(Schedule(
                id = updateId,
                date = LocalDate.of(selectPickerYear,selectPickerMonth,selectPickerDay),
                startTime = LocalTime.of(selectPickerStartTime.hours,selectPickerStartTime.minutes,0),
                endTime = LocalTime.of(selectPickerEndTime.hours,selectPickerEndTime.minutes,0),
                icon = imageToInt(selectScheduleImage,typedSchedule),
                title = inputScheduleText,
                comment = inputCommentText
            ))
        }
    }

}