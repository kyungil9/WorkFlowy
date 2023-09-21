package com.beank.workFlowy.screen.schedule

import android.content.res.TypedArray
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import com.beank.data.mapper.intToLocalDate
import com.chargemap.compose.numberpicker.FullHours
import com.chargemap.compose.numberpicker.Hours
import com.beank.domain.model.Schedule
import com.beank.domain.repository.LogRepository
import com.beank.domain.usecase.ScheduleUsecases
import com.beank.domain.usecase.schedule.InsertSchedule
import com.beank.domain.usecase.schedule.UpdateSchedule
import com.beank.workFlowy.R
import com.beank.workFlowy.screen.WorkFlowyViewModel
import com.beank.workFlowy.utils.changeDayInfo
import com.beank.workFlowy.utils.fromScheduleJson
import com.beank.workFlowy.utils.imageToInt
import com.beank.workFlowy.utils.intToImage
import com.beank.workFlowy.utils.transDayToShortKorean
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

    private val _scheduleUiState = MutableStateFlow(ScheduleUiState())
    private val _inputScheduleText = MutableStateFlow("")
    private val _selectScheduleImage = MutableStateFlow(R.drawable.baseline_calendar_today_24)
    private val _selectPickerYear = MutableStateFlow(LocalDate.now().year)
    private val _selectPickerMonth = MutableStateFlow(LocalDate.now().monthValue)
    private val _selectPickerDay = MutableStateFlow(LocalDate.now().dayOfMonth)
    private val _selectPickerStartTime = MutableStateFlow(FullHours(LocalTime.now().hour,LocalTime.now().minute))
    private val _selectPickerEndTime = MutableStateFlow(FullHours(LocalTime.now().hour,LocalTime.now().minute))
    private val _endMonthDay = MutableStateFlow(changeDayInfo(LocalDate.now()))
    private val _inputCommentText = MutableStateFlow("")
    private lateinit var typedSchedule : TypedArray

    var updateId = ""
    val scheduleUiState get() = _scheduleUiState.asStateFlow()
    val inputScheduleText get() = _inputScheduleText.asStateFlow()
    val selectScheduleImage get() = _selectScheduleImage.asStateFlow()
    val selectPickerYear get() = _selectPickerYear.asStateFlow()
    val selectPickerMonth get() = _selectPickerMonth.asStateFlow()
    val selectPickerDay get() = _selectPickerDay.asStateFlow()
    val selectPickerStartTime get() = _selectPickerStartTime.asStateFlow()
    val selectPickerEndTime get() = _selectPickerEndTime.asStateFlow()
    val endMonthDay get() = _endMonthDay.asStateFlow()
    val inputCommentText get() = _inputCommentText.asStateFlow()

    fun initScheduleImages(scheduleList : TypedArray){
        typedSchedule = scheduleList
        launchCatching {
            val scheduleImages = ArrayList<Int>()
            for (i in 0 until scheduleList.length()){
                scheduleImages.add(scheduleList.getResourceId(i,0))
            }
            _scheduleUiState.update { state -> state.copy(scheduleImageList = scheduleImages) }
        }
        val today = savedStateHandle.get<Int>("today")!!
        if (today == 0){
            val schedule = savedStateHandle.get<String>("schedule")?.let {json ->
                json.fromScheduleJson()
            }!!
            updateId = schedule.id!!
            _inputScheduleText.value = schedule.title
            _inputCommentText.value = schedule.comment
            _selectPickerYear.value = schedule.date.year
            _selectPickerMonth.value = schedule.date.monthValue
            _selectPickerDay.value = schedule.date.dayOfMonth
            _selectPickerStartTime.value = FullHours(schedule.startTime.hour,schedule.startTime.minute)
            _selectPickerEndTime.value = FullHours(schedule.endTime.hour,schedule.endTime.minute)
            _selectScheduleImage.value = intToImage(schedule.icon,typedSchedule)
        }else{
            val day = today.intToLocalDate()
            _selectPickerYear.value = day.year
            _selectPickerMonth.value = day.monthValue
            _selectPickerDay.value = day.dayOfMonth
        }
    }

    fun updateScheduleText(text : String){
        _inputScheduleText.value = text
    }

    fun updateScheduleImage(image : Int){
        _selectScheduleImage.value = image
    }

    fun updateSelectYear(year : Int){
        _selectPickerDay.value = 1//임시방편
        _selectPickerYear.value = year
        _endMonthDay.value = changeDayInfo(LocalDate.of(selectPickerYear.value,selectPickerMonth.value,1))
    }

    fun updateSelectMonth(month : Int){
        _selectPickerDay.value = 1
        _selectPickerMonth.value = month
        _endMonthDay.value = changeDayInfo(LocalDate.of(selectPickerYear.value,selectPickerMonth.value,1))
    }

    fun updateSelectDay(day : Int){
        _selectPickerDay.value = day
    }

    fun updateSelectStartTime(time : Hours){
        _selectPickerStartTime.value = FullHours(time.hours,time.minutes)
    }

    fun updateSelectEndTime(time : Hours){
        _selectPickerEndTime.value = FullHours(time.hours,time.minutes)
    }

    fun selectDayOfWeek(year: Int,month: Int,day: Int) : String{
        return transDayToShortKorean(LocalDate.of(year,month,day).dayOfWeek.value)
    }

    fun updateCommentText(text: String){
        _inputCommentText.value = text
    }

    fun insertScheduleInfo(){
        launchCatching {
            scheduleUsecases.insertSchedule(Schedule(
                id = null,
                date = LocalDate.of(selectPickerYear.value,selectPickerMonth.value,selectPickerDay.value),
                startTime = LocalTime.of(selectPickerStartTime.value.hours,selectPickerStartTime.value.minutes,0),
                endTime = LocalTime.of(selectPickerEndTime.value.hours,selectPickerEndTime.value.minutes,0),
                icon = imageToInt(selectScheduleImage.value,typedSchedule),
                title = inputScheduleText.value,
                comment = inputCommentText.value
            ))
        }
    }

    fun updateScheduleInfo(){
        launchCatching {
            scheduleUsecases.updateSchedule(Schedule(
                id = updateId,
                date = LocalDate.of(selectPickerYear.value,selectPickerMonth.value,selectPickerDay.value),
                startTime = LocalTime.of(selectPickerStartTime.value.hours,selectPickerStartTime.value.minutes,0),
                endTime = LocalTime.of(selectPickerEndTime.value.hours,selectPickerEndTime.value.minutes,0),
                icon = imageToInt(selectScheduleImage.value,typedSchedule),
                title = inputScheduleText.value,
                comment = inputCommentText.value
            ))
        }
    }

}