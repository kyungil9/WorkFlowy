package com.beank.workFlowy.screen.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.workDataOf
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
import com.beank.workFlowy.utils.MessageMode
import com.beank.workFlowy.utils.toFormatString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import com.beank.presentation.R.string as AppText

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class WeekViewModel @Inject constructor(
    private val weekUsecases: WeekUsecases,
    private val workManager: WorkManager,
    private val messageRequest : OneTimeWorkRequest.Builder,
    logRepository: LogRepository
) : WorkFlowyViewModel(logRepository) {

    private var oldTimeMills : Long = 0
    private val _selectDayFlow = MutableStateFlow<LocalDate>(LocalDate.now())
    val selectDayFlow get() = _selectDayFlow.asStateFlow()
    val selectDayStringFlow = selectDayFlow
        .map { "< ${it.toFormatString()} >" }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(),"")
    val uiState = WeekUiState()

    private val scheduleInfo get() = uiState.selectSchedule
    private var todayJob : Job? = null

    val timerJob = viewModelScope.launch(start = CoroutineStart.LAZY) {
        withContext(Dispatchers.IO){
            oldTimeMills = System.currentTimeMillis()
            while (true) {
                val delayMills = System.currentTimeMillis() - oldTimeMills
                if (delayMills >= 1000L) {//1초당
                    val record = uiState.recordList
                    if (record.isNotEmpty()) {
                        uiState.progressTime = Duration.between(record[0].startTime, LocalDateTime.now())
                    }
                    oldTimeMills = System.currentTimeMillis()
                }
            }
        }
    }

    init {
        getAllTagInfo()
        getTodaySchedule()
        getSelectedRecordInfo()
    }
    fun setSelectScheduleInfo(schedule: Schedule){
        uiState.selectSchedule = schedule
    }
    fun onWeekStateChange(value : Boolean){
        uiState.weekState = value
    }

    fun onScheduleStateChange(value: Boolean){
        uiState.scheduleState = value
    }

    fun onSelectDayChange(day : LocalDate){
        val beforeIndex = onDateIndexGet(selectDayFlow.value)
        val beforeList = uiState.weekDayList[beforeIndex]
        uiState.weekDayList[beforeIndex] = beforeList.copy(isChecked = false)
        _selectDayFlow.value = day
        val afterIndex = onDateIndexGet(day)
        val afterList = uiState.weekDayList[afterIndex]
        uiState.weekDayList[afterIndex] = afterList.copy(isChecked = true)
        onScheduleStateChange(false)
    }

    fun plusSelectDay() : LocalDate {
        val beforeIndex = onDateIndexGet(selectDayFlow.value)
        val beforeList = uiState.weekDayList[beforeIndex]
        uiState.weekDayList[beforeIndex] = beforeList.copy(isChecked = false)
        _selectDayFlow.value = selectDayFlow.value.plusDays(1)
        onScheduleStateChange(false)
        val afterIndex = onDateIndexGet(_selectDayFlow.value)
        val afterList = uiState.weekDayList[afterIndex]
        uiState.weekDayList[afterIndex] = afterList.copy(isChecked = true)

        return selectDayFlow.value
    }


    fun minusSelectDay() : LocalDate {
        val beforeIndex = onDateIndexGet(selectDayFlow.value)
        val beforeList = uiState.weekDayList[beforeIndex]
        uiState.weekDayList[beforeIndex] = beforeList.copy(isChecked = false)
        _selectDayFlow.value = selectDayFlow.value.minusDays(1)
        onScheduleStateChange(false)
        val afterIndex = onDateIndexGet(_selectDayFlow.value)
        val afterList = uiState.weekDayList[afterIndex]
        uiState.weekDayList[afterIndex] = afterList.copy(isChecked = true)

        return selectDayFlow.value
    }

    private fun onDateIndexGet(date: LocalDate) = ChronoUnit.DAYS.between(LocalDate.of(2021, 12, 28), date).toInt()

    fun onScheduleDelete(){
        launchCatching {
            weekUsecases.deleteSchedule(scheduleInfo)
            val messageWorkRequest = messageRequest //등록된 알람 취소
                .setInputData(
                    workDataOf(
                    "mode" to MessageMode.CANCLE,
                    "id" to scheduleInfo.alarmCode
                )
                )
                .setBackoffCriteria(BackoffPolicy.LINEAR,30000, TimeUnit.MILLISECONDS)
                .build()
            workManager.enqueue(messageWorkRequest)
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
                val record = uiState.recordList[0]
                if (uiState.recordList[0].date != LocalDate.now()){//하루가 넘게 기록되고있는경우 쪼개기
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
                    val today = LocalDate.now()
                    val endTime = LocalDateTime.now()
                    val startTime = LocalDateTime.of(today, LocalTime.of(0,0))
                    val progressTime = Duration.between(startTime, endTime).toMinutes()
                    weekUsecases.updateRecord(
                        id = record.id!!,
                        startTime = startTime,
                        endTime = endTime,
                        progressTime = progressTime,
                        date = today
                    )
                }else {
                    val endTime = LocalDateTime.now()
                    val progressTime = Duration.between(record.startTime, endTime).toMinutes()
                    weekUsecases.updateRecord(
                        id = record.id!!,
                        endTime = endTime,
                        progressTime = progressTime,
                        pause = true
                    )
                }
            }
        }
    }

    private fun getAllTagInfo() = weekUsecases.getAllTag()
        .flowOn(Dispatchers.IO).onEach { state ->
            state.onEmpty {
                uiState.tagList = emptyList()
            }
            state.onSuccess { tagList ->
                uiState.tagList = tagList
            }
            state.onException { message, e ->
                e.message?.let {
                    if (!it.contains("PERMISSION_DENIED")){
                        SnackbarManager.showMessage(AppText.firebase_server_error)
                        logFirebaseFatalCrash(message, e)
                    }
                }
            }
        }.launchIn(viewModelScope)

    private fun getSelectedRecordInfo() = weekUsecases.getNowRecord(true)
        .flowOn(Dispatchers.IO).onEach { state ->
            state.onLoading { //프로그래스바 실행
                uiState.actProgress = true
            }
            state.onSuccess { nowRecord ->
                uiState.recordList = listOf(nowRecord.record)
                uiState.selectTag = nowRecord.tag
                uiState.actProgress = false
                //uiState.progressTime = Duration.between(nowRecord.record.startTime, LocalDateTime.now())
            }
            state.onException { message, e ->
                uiState.actProgress = false
                e.message?.let {
                    if (!it.contains("PERMISSION_DENIED")){
                        SnackbarManager.showMessage(AppText.firebase_server_error)
                        logFirebaseFatalCrash(message, e)
                    }
                }
            }
        }.launchIn(viewModelScope)

    private fun getTodaySchedule() {
        launchCatching{
            selectDayFlow.collectLatest{date ->
                todayJob?.cancel()
                todayJob = weekUsecases.getTodaySchedule(date)
                    .flowOn(Dispatchers.IO).onEach { state ->
                        state.onSuccess {scheduleList ->
                            uiState.scheduleList = scheduleList.sortedWith(compareBy({it.check},{ it.time.not() },{it.startTime}))
                        }
                        state.onException { message, e ->
                            e.message?.let {
                                if (!it.contains("PERMISSION_DENIED")){
                                    SnackbarManager.showMessage(AppText.firebase_server_error)
                                    logFirebaseFatalCrash(message, e)
                                }
                            }
                        }
                        state.onEmpty {
                            uiState.scheduleList = emptyList()
                        }
                    }.launchIn(viewModelScope)
            }
        }
    }


}