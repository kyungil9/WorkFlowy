package com.beank.workFlowy.screen.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Stable
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
import com.beank.domain.usecase.record.GetNowRecord
import com.beank.domain.usecase.record.StartNewRecord
import com.beank.domain.usecase.schedule.DeleteSchedule
import com.beank.domain.usecase.schedule.GetTodaySchedule
import com.beank.domain.usecase.tag.DeleteTag
import com.beank.domain.usecase.tag.GetAllTag
import com.beank.workFlowy.component.snackbar.SnackbarManager
import com.beank.workFlowy.screen.WorkFlowyViewModel
import com.beank.workFlowy.utils.toFormatString
import com.beank.workFlowy.utils.transDayToKorean
import com.beank.workFlowy.utils.zeroFormat
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject
import com.beank.presentation.R.string as AppText

@Stable
data class WeekUiState(
    val tagList : List<Tag> = emptyList(),
    val scheduleList : List<Schedule> = emptyList(),
    val recordList : List<Record> = emptyList()
)

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
    var selectedTag by mutableStateOf(Tag())
        private set
    var progressTime by mutableStateOf(Duration.ZERO)
        private set
    var actBoxProgress by mutableStateOf(true)
        private set
    var weekState by mutableStateOf(false)
        private set
    var scheduleState by mutableStateOf(false)
        private set
    var scheduleInfo = Schedule()
        private set
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
                        progressTime = Duration.between(record[0].startTime, LocalDateTime.now())
                    }
                    oldTimeMills = System.currentTimeMillis()
                }
            }
        }
    }

    fun setSelectScheduleInfo(schedule: Schedule){
        scheduleInfo = schedule
    }
    fun changeWeekState(value : Boolean){
        weekState = value
    }

    fun changeScheduleState(value: Boolean){
        scheduleState = value
    }

    fun changeSelectDay(day : LocalDate){
        _selectDayFlow.value = day
    }

    fun plusSelectDay() : LocalDate {
        _selectDayFlow.value = selectDayFlow.value.plusDays(1)
        return selectDayFlow.value
    }


    fun minusSelectDay() : LocalDate {
        _selectDayFlow.value = selectDayFlow.value.minusDays(1)
        return selectDayFlow.value
    }

    fun deleteSelectSchedule(){
        launchCatching {
            weekUsecases.deleteSchedule(scheduleInfo)
        }
    }

    fun deleteSelectTag(tag: Tag){
        launchCatching {
            weekUsecases.deleteTag(tag)
        }
    }

    fun changeRecordInfo(tag: Tag){
        if (uiState.recordList.isNotEmpty()){
            launchCatching {
                val endTime = LocalDateTime.now()
                val progressTime = Duration.between(uiState.recordList[0].startTime,endTime).toMinutes()
                weekUsecases.startNewRecord(
                    id = uiState.recordList[0].id!!,
                    endTime = endTime,
                    progressTime = progressTime,
                    pause = false,
                    record = Record(tag = tag.title)
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
                actBoxProgress = true

            }
            state.onSuccess { nowRecord ->
                actBoxProgress = false
                uiState = uiState.copy(recordList = listOf(nowRecord.record))
                selectedTag = nowRecord.tag
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