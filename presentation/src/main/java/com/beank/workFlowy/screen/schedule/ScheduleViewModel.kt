package com.beank.workFlowy.screen.schedule

import android.content.res.TypedArray
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.beank.domain.model.Schedule
import com.beank.domain.repository.LogRepository
import com.beank.domain.usecase.ScheduleUsecases
import com.beank.workFlowy.screen.WorkFlowyViewModel
import com.beank.workFlowy.utils.MessageMode
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
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val savedStateHandle : SavedStateHandle,
    private val scheduleUsecases: ScheduleUsecases,
    private val workManager: WorkManager,
    private val messageRequest : OneTimeWorkRequest.Builder,
    logRepository: LogRepository
) : WorkFlowyViewModel(logRepository){

    private lateinit var typedSchedule : TypedArray
    val alarmList = listOf("5분전","30분전","1시간전","3시간전","6시간전","12시간전","하루전")
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
            uiState.originalSchedule = schedule
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
            val alarmTime = onAlarmTimeCalculate()
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
                alarmTime = alarmTime,
                alarmState = uiState.alarmState
            ))
            //알람 등록 추가
            //알람 하루전에 해당되는 경우 알람매니저 바로 추가
            if (uiState.alarmToggle){
                if (LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(0,0)).isBefore(alarmTime) && LocalDateTime.now().isAfter(alarmTime)){
                    onNowMessageBuild(alarmTime)
                }
            }
        }
    }

    private fun onNowMessageBuild(alarmTime : LocalDateTime){
        val messageWorkRequest = messageRequest
            .setInputData(workDataOf(
                "mode" to MessageMode.NOW,
                "title" to uiState.title,
                "body" to uiState.comment,
                "time" to alarmTime,
                "id" to uiState.originalSchedule.alarmCode
            ))
            .setBackoffCriteria(BackoffPolicy.LINEAR,30000, TimeUnit.MILLISECONDS)
            .build()
        workManager.enqueue(messageWorkRequest)
    }

    private fun onCheckTime() : Boolean {
        return (LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(0,0)).isBefore(uiState.originalSchedule.alarmTime))&& (LocalDateTime.now().isAfter(uiState.originalSchedule.alarmTime))
    }

    fun onScheduleUpdate(){
        launchCatching {
            val alarmTime = onAlarmTimeCalculate()
            //알람 켜지거나 꺼졌을때
            if (uiState.alarmToggle){
                if (uiState.alarmToggle != uiState.originalSchedule.alarm || uiState.alarmState != uiState.originalSchedule.alarmState){//알람 내역에 변경사항이 있는경우
                    onNowMessageBuild(alarmTime)
                }

            }else{
                if (uiState.alarmToggle != uiState.originalSchedule.alarm){//알람 설정을 끈 경우
                    if (onCheckTime()){
                        val messageWorkRequest = messageRequest //등록된 알람 취소
                            .setInputData(workDataOf(
                                "mode" to MessageMode.CANCLE,
                                "id" to uiState.originalSchedule.alarmCode
                            ))
                            .setBackoffCriteria(BackoffPolicy.LINEAR,30000, TimeUnit.MILLISECONDS)
                            .build()
                        workManager.enqueue(messageWorkRequest)
                    }
                }
            }
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
                alarmTime = alarmTime,
                alarmState = uiState.alarmState,
                alarmCode = uiState.originalSchedule.alarmCode
            ))
        }
    }

}