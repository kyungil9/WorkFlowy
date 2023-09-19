package com.beank.workFlowy.screen.home

import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import com.beank.domain.model.Record
import com.beank.domain.model.Schedule
import com.beank.domain.model.Tag
import com.beank.domain.repository.LogRepository
import com.beank.domain.usecase.record.GetNowRecord
import com.beank.domain.usecase.record.InsertRecord
import com.beank.domain.usecase.record.StartNewRecord
import com.beank.domain.usecase.record.UpdateRecord
import com.beank.domain.usecase.schedule.DeleteSchedule
import com.beank.domain.usecase.schedule.GetTodaySchedule
import com.beank.domain.usecase.tag.DeleteTag
import com.beank.domain.usecase.tag.GetAllTag
import com.beank.workFlowy.screen.WorkFlowyViewModel
import com.beank.workFlowy.utils.transDayToKorean
import com.beank.workFlowy.utils.zeroFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@Stable
data class WeekUiState(
    val tagList : List<Tag> = emptyList(),
    val scheduleList : List<Schedule> = emptyList(),
    val recordList : List<Record> = emptyList()
)

@HiltViewModel
class WeekViewModel @Inject constructor(
    private val deleteSchedule: DeleteSchedule,
    private val deleteTag: DeleteTag,
    private val getTodaySchedule: GetTodaySchedule,
    private val getNowRecord: GetNowRecord,
    private val getAllTag: GetAllTag,
    private val startNewRecord: StartNewRecord,
    logRepository: LogRepository
) : WorkFlowyViewModel(logRepository) {

    private var oldTimeMills : Long = 0
    private val _progressTimeFlow = MutableStateFlow(Duration.ZERO)
    private val _uiState = MutableStateFlow(WeekUiState())
    private val _selectDayFlow = MutableStateFlow<LocalDate>(LocalDate.now())
    private val _selectedTagFlow = MutableStateFlow(Tag())
    val selectDayFlow get() = _selectDayFlow.asStateFlow()
    val uiState get() = _uiState.asStateFlow()
    val selectedTagFlow get() = _selectedTagFlow.asStateFlow()
    val progressTimeFlow get() = _progressTimeFlow.asStateFlow()
    val selectDayStringFlow get() = _selectDayFlow.asStateFlow().map { "< ${it.year%100}/${zeroFormat.format(it.monthValue)}/${zeroFormat.format(it.dayOfMonth)} ${transDayToKorean(it.dayOfWeek.value)} >" }

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
                    val record = uiState.value.recordList
                    if (!record.isNullOrEmpty()) {
                        _progressTimeFlow.value =
                            Duration.between(record[0].startTime, LocalDateTime.now())
                    }
                    oldTimeMills = System.currentTimeMillis()
                }
            }
        }
    }

    fun changeSelectDay(day : LocalDate){
        _selectDayFlow.value = day
    }

    fun deleteSelectSchedule(schedule: Schedule){
        launchCatching {
            deleteSchedule(schedule)
        }
    }

    fun deleteSelectTag(tag: Tag){
        launchCatching {
            deleteTag(tag)
        }
    }

    fun changeRecordInfo(tag: Tag){
        if (uiState.value.recordList.isNotEmpty()){
            launchCatching {
                val endTime = LocalDateTime.now()
                val progressTime = Duration.between(uiState.value.recordList[0].startTime,endTime).toMinutes()
                startNewRecord(
                    id = uiState.value.recordList[0].id!!,
                    endTime = endTime,
                    progressTime = progressTime,
                    pause = false,
                    record = Record(tag = tag.title)
                )
            }
        }
    }

    private fun getAllTagInfo() = getAllTag()
        .onEach { tagList ->
            _uiState.update { state -> state.copy(tagList = tagList) }
        }.launchIn(viewModelScope)

    private fun getSelectedRecordInfo() = getNowRecord(true)
        .onEach { nowRecord ->
            nowRecord.record.id?.let {
                _uiState.update { state -> state.copy(recordList = listOf(nowRecord.record)) }
                _selectedTagFlow.value = nowRecord.tag
            }
        }.launchIn(viewModelScope)

    private fun getTodaySchedule() {
        viewModelScope.launch (Dispatchers.IO){
            selectDayFlow.collect{
                getTodaySchedule(it)
                    .onEach { scheduleList ->
                        _uiState.update { state -> state.copy(scheduleList = scheduleList) }
                    }.launchIn(viewModelScope)
            }
        }
    }


}