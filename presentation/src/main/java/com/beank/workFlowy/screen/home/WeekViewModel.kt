package com.beank.workFlowy.screen.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.beank.domain.model.Record
import com.beank.domain.model.Schedule
import com.beank.domain.model.Tag
import com.beank.domain.model.onEmpty
import com.beank.domain.model.onException
import com.beank.domain.model.onLoading
import com.beank.domain.model.onSuccess
import com.beank.domain.repository.LogRepository
import com.beank.domain.usecase.WeekUsecases
import com.beank.workFlowy.component.snackbar.SnackbarManager
import com.beank.workFlowy.screen.WorkFlowyViewModel
import com.beank.workFlowy.utils.toFormatString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import com.beank.presentation.R.string as AppText

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class WeekViewModel @Inject constructor(
    private val weekUsecases: WeekUsecases,
    logRepository: LogRepository
) : WorkFlowyViewModel(logRepository) {

    private var oldTimeMills : Long = 0
    private val _selectDayFlow = MutableStateFlow<LocalDate>(LocalDate.now())
    val selectDayFlow get() = _selectDayFlow.asStateFlow()
    val selectDayStringFlow = selectDayFlow
        .map { "< ${it.toFormatString()} >" }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(),"")
    var uiState by mutableStateOf(WeekUiState())
        private set
    private val scheduleInfo get() = uiState.selectSchedule
    var todayJob : Job? = null

    init {
        getAllTagInfo()
        getTodaySchedule()
        getSelectedRecordInfo()
    }

    val timerJob = viewModelScope.launch(start = CoroutineStart.LAZY) {
        withContext(Dispatchers.IO){
            oldTimeMills = System.currentTimeMillis()
            while (true){
                val delayMills = System.currentTimeMillis() - oldTimeMills
                if (delayMills >= 1000L) {
                    val record = uiState.recordList
                    if (record.isNotEmpty()) {
                        uiState = uiState.copy(progressTime = Duration.between(record[0].startTime, LocalDateTime.now()))
                    }
                    oldTimeMills = System.currentTimeMillis()
                }
            }
        }
    }

    fun setSelectScheduleInfo(schedule: Schedule){
        uiState = uiState.copy(selectSchedule = schedule)
    }
    fun onWeekStateChange(value : Boolean){
        uiState = uiState.copy(weekState = value)
    }

    fun onScheduleStateChange(value: Boolean){
        uiState = uiState.copy(scheduleState = value)
    }

    fun onSelectDayChange(day : LocalDate){
        _selectDayFlow.value = day
        onScheduleStateChange(false)
    }

    fun plusSelectDay() : LocalDate {
        _selectDayFlow.value = selectDayFlow.value.plusDays(1)
        onScheduleStateChange(false)
        return selectDayFlow.value
    }


    fun minusSelectDay() : LocalDate {
        _selectDayFlow.value = selectDayFlow.value.minusDays(1)
        onScheduleStateChange(false)
        return selectDayFlow.value
    }

    fun onScheduleDelete(){
        launchCatching {
            weekUsecases.deleteSchedule(scheduleInfo)
        }
    }

    fun onTagDelete(tag: Tag){
        launchCatching {
            weekUsecases.deleteTag(tag)
        }
    }

    fun onCheckScheduleChange(){
        launchCatching {
            scheduleInfo.check = scheduleInfo.check.not()
            weekUsecases.updateCheckSchedule(scheduleInfo.id!!,scheduleInfo.check)
        }
    }

    fun onRecordChange(tag: Tag){
        if (uiState.recordList.isNotEmpty()){
            launchCatching {
                val record = uiState.recordList[0]
                val endTime = LocalDateTime.now()
                val progressTime =
                    if (ChronoUnit.DAYS.between(record.startTime.toLocalDate(),record.date) == 0L)
                        Duration.between(record.startTime,endTime).toMinutes()
                    else
                        Duration.between(LocalDateTime.of(record.date, LocalTime.of(0,0,0)),endTime).toMinutes()
                weekUsecases.startNewRecord(
                    id = record.id!!,
                    endTime = endTime,
                    progressTime = progressTime,
                    pause = false,
                    record = Record(tag = tag.title)
                )
            }
        }
    }

    fun onRecordReduce(){
        if (uiState.recordList.isNotEmpty()){
            launchCatching {
                if (uiState.recordList[0].date != LocalDate.now()){//하루가 넘게 기록되고있는경우 쪼개기
                    val record = uiState.recordList[0]
                    var startDay = record.date
                    for (i in 0 until ChronoUnit.DAYS.between(record.date,LocalDate.now()).toInt()){
                        val startTime = if (i == 0) record.startTime else LocalDateTime.of(startDay, LocalTime.of(0,0,0))
                        val endTime = LocalDateTime.of(startDay, LocalTime.of(23, 59, 59))
                        val progressTime = Duration.between(startTime, endTime).toMinutes()
                        weekUsecases.insertRecord(
                            Record(
                                id = null,
                                tag = record.tag,
                                date = startDay,
                                startTime = startTime,
                                endTime = endTime,
                                progressTime = progressTime,
                                pause = false
                            )
                        )
                        startDay = startDay.plusDays(1)
                    }
                }
                val endTime = LocalDateTime.now()
                val progressTime = Duration.between(uiState.recordList[0].startTime,endTime).toMinutes()
                weekUsecases.updateRecord(
                    id = uiState.recordList[0].id!!,
                    endTime = endTime,
                    progressTime = progressTime,
                    pause = true,
                    date = LocalDate.now()
                )
            }
        }
    }

    private fun getAllTagInfo() = weekUsecases.getAllTag()
        .flowOn(Dispatchers.IO).onEach { state ->
            state.onSuccess { tagList ->
                uiState = uiState.copy(tagList = tagList)
            }
            state.onException { message, e ->
                SnackbarManager.showMessage(AppText.firebase_server_error)
                logFirebaseFatalCrash(message,e)
            }
        }.launchIn(viewModelScope)

    private fun getSelectedRecordInfo() = weekUsecases.getNowRecord(true)
        .flowOn(Dispatchers.IO).onEach { state ->
            state.onLoading { //프로그래스바 실행
                uiState = uiState.copy(actProgress = true)

            }
            state.onSuccess { nowRecord ->
                uiState = uiState.copy(recordList = listOf(nowRecord.record), selectTag = nowRecord.tag, actProgress = false)
            }
            state.onException { message, e ->
                SnackbarManager.showMessage(AppText.firebase_server_error)
                logFirebaseFatalCrash(message,e)
            }
        }.launchIn(viewModelScope)

    private fun getTodaySchedule() {
        launchCatching{
            selectDayFlow.collectLatest{
                todayJob?.cancel()
                todayJob = weekUsecases.getTodaySchedule(it)
                    .flowOn(Dispatchers.IO).onEach { state ->
                        state.onSuccess {scheduleList ->
                            uiState = uiState.copy(scheduleList = scheduleList)
                        }
                        state.onException { message, e ->
                            SnackbarManager.showMessage(AppText.firebase_server_error)
                            logFirebaseFatalCrash(message,e)
                        }
                        state.onEmpty {
                            uiState = uiState.copy(scheduleList = emptyList())
                        }
                    }.launchIn(viewModelScope)
            }
        }
    }


}