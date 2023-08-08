package com.example.workFlowy.screen.Home

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Record
import com.example.domain.model.Schedule
import com.example.domain.model.Tag
import com.example.domain.usecase.RecordUsecase
import com.example.domain.usecase.ScheduleUsecase
import com.example.domain.usecase.TagUsecase
import com.example.workFlowy.R
import com.example.workFlowy.utils.transDayToKorean
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

@Stable
data class WeekUiState(
    val tagList : List<Tag> = emptyList(),
    val scheduleList : List<Schedule> = emptyList(),
    val recordList : List<Record> = emptyList()
)

@HiltViewModel
class WeekViewModel @Inject constructor(
    private val recordUsecase: RecordUsecase,
    private val scheduleUsecase: ScheduleUsecase,
    private val tagUsecase: TagUsecase
) : ViewModel() {

    private val _progressTimeFlow = MutableStateFlow(Duration.ZERO)
    private val _uiState = MutableStateFlow(WeekUiState())
    private val _selectDayFlow = MutableStateFlow<LocalDate>(LocalDate.now())
    private val _selectedTagFlow = MutableStateFlow<Tag>(Tag(null,
        R.drawable.baseline_check_box_outline_blank_24, ""))
    val selectDayFlow get() = _selectDayFlow.asStateFlow()
    val uiState get() = _uiState.asStateFlow()
    val selectedTagFlow get() = _selectedTagFlow.asStateFlow()
    val progressTimeFlow get() = _progressTimeFlow.asStateFlow()
    val selectDayStringFlow get() = _selectDayFlow.asStateFlow().map { "< ${it.year%100}/${it.monthValue}/${it.dayOfMonth} ${transDayToKorean(it.dayOfWeek.value)} >" }

    val timer = Timer()
    init {
        initTag()
        getAllTagInfo()
        getTodaySchedule()
        getSelectedRecordInfo()
        initRecord()
    }

    private fun initTag(){
        viewModelScope.launch(Dispatchers.IO) {
            if (tagUsecase.getTagSize() <= 0) {
                tagUsecase.insertTag(Tag(null, R.drawable.baseline_menu_book_24, "공부중"))
                tagUsecase.insertTag(Tag(null, R.drawable.baseline_directions_run_24, "운동중"))
                tagUsecase.insertTag(Tag(null, R.drawable.baseline_bed_24, "휴식중"))
                tagUsecase.insertTag(Tag(null, R.drawable.baseline_directions_bus_24, "이동중"))
                tagUsecase.insertTag(Tag(null, R.drawable.baseline_hotel_24, "수면중"))
                tagUsecase.insertTag(Tag(null, R.drawable.baseline_local_cafe_24, "개인시간"))
            }
        }
    }

    private fun initRecord(){
        viewModelScope.launch (Dispatchers.IO){
            if (recordUsecase.getRecordSize() <= 0) {
                recordUsecase.insertRecord(
                    Record(
                        id = 0,
                        tag = "개인시간",
                        startTime = LocalDateTime.now(),
                        endTime = null,
                        progressTime = 0,
                        pause = true
                    ))
                _selectedTagFlow.value = Tag(0, R.drawable.baseline_local_cafe_24, "개인시간")
            }
        }
    }

    val timerTask = object : TimerTask(){
        override fun run() {
            val record = uiState.value.recordList
            if (!record.isNullOrEmpty()){
                _progressTimeFlow.value = Duration.between(record[0].startTime, LocalDateTime.now())
            }
        }
    }

    fun changeSelectDay(day : LocalDate){
        _selectDayFlow.value = day
    }

    fun insertRecord(tag : Tag){
        viewModelScope.launch(Dispatchers.IO) {
            recordUsecase.insertRecord(
                Record(
                    id = 0,
                    tag = tag.title,
                    startTime = LocalDateTime.now(),
                    endTime = null,
                    progressTime = 0,
                    pause = true
            ))
        }
    }

    fun insertSchedule(schedule: Schedule){
        viewModelScope.launch(Dispatchers.IO) {
            scheduleUsecase.insertSchedule(schedule)
        }
    }

    fun changeRecordInfo(){
        if (uiState.value.recordList.isNotEmpty()){
            viewModelScope.launch(Dispatchers.IO) {
                val endTime = LocalDateTime.now()
                val progressTime = Duration.between(uiState.value.recordList[0].startTime,endTime).toMinutes()
                recordUsecase.updateRecord(
                    endTime = endTime,
                    progressTime = progressTime,
                    id = uiState.value.recordList[0].id,
                    pause = false
                )
            }
        }
    }

    private fun getAllTagInfo() = tagUsecase.getTagInfo()
        .onEach { tagList ->
            _uiState.update { state -> state.copy(tagList = tagList) }
        }.launchIn(viewModelScope)

    private fun getSelectedRecordInfo() = recordUsecase.getPauseRecord(true)
        .onEach { recordList ->
            _uiState.update { state -> state.copy(recordList = recordList) }
            viewModelScope.launch(Dispatchers.IO) {
                if (recordList.isNotEmpty())
                    _selectedTagFlow.value = tagUsecase.getTagSingleInfo(recordList[0].tag)
            }
        }.launchIn(viewModelScope)

    private fun getTodaySchedule() {
        viewModelScope.launch (Dispatchers.IO){
            selectDayFlow.collect{
                scheduleUsecase.getScheduleInfo(selectDayFlow.value)
                    .onEach { scheduleList ->
                        _uiState.update { state -> state.copy(scheduleList = scheduleList) }
                    }.launchIn(viewModelScope)
            }
        }
    }


}