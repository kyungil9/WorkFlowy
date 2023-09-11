package com.beank.workFlowy.screen.home

import android.content.res.TypedArray
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beank.domain.model.Record
import com.beank.domain.model.Schedule
import com.beank.domain.model.Tag
import com.beank.domain.usecase.RecordUsecase
import com.beank.domain.usecase.ScheduleUsecase
import com.beank.domain.usecase.TagUsecase
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
    private val recordUsecase: RecordUsecase,
    private val scheduleUsecase: ScheduleUsecase,
    private val tagUsecase: TagUsecase
) : ViewModel() {

    private lateinit var tagImages : TypedArray
    private lateinit var scheduleImages : TypedArray
    private var oldTimeMills : Long = 0
    private val _progressTimeFlow = MutableStateFlow(Duration.ZERO)
    private val _uiState = MutableStateFlow(WeekUiState())
    private val _selectDayFlow = MutableStateFlow<LocalDate>(LocalDate.now())
    private val _selectedTagFlow = MutableStateFlow<Tag>(Tag(null,
        0, ""))
    val selectDayFlow get() = _selectDayFlow.asStateFlow()
    val uiState get() = _uiState.asStateFlow()
    val selectedTagFlow get() = _selectedTagFlow.asStateFlow()
    val progressTimeFlow get() = _progressTimeFlow.asStateFlow()
    val selectDayStringFlow get() = _selectDayFlow.asStateFlow().map { "< ${it.year%100}/${zeroFormat.format(it.monthValue)}/${zeroFormat.format(it.dayOfMonth)} ${transDayToKorean(it.dayOfWeek.value)} >" }

    init {
        initTag()
        getAllTagInfo()
        getTodaySchedule()
        getSelectedRecordInfo()
        initRecord()
    }

    //tag를 string으로 변경하여 int코드 변경에 상관없이 해결
    private fun initTag(){
        viewModelScope.launch(Dispatchers.IO) {
            if (tagUsecase.getTagSize() <= 0) {
                tagUsecase.insertTag(Tag(null, 0, "공부중"))
                tagUsecase.insertTag(Tag(null, 1, "운동중"))
                tagUsecase.insertTag(Tag(null,2, "휴식중"))
                tagUsecase.insertTag(Tag(null, 3, "이동중"))
                tagUsecase.insertTag(Tag(null, 4, "수면중"))
                tagUsecase.insertTag(Tag(null, 5, "개인시간"))
            }
        }
    }

    private fun initRecord(){
        viewModelScope.launch (Dispatchers.IO){
            if (recordUsecase.getRecordSize() <= 0) {
                recordUsecase.insertRecord(
                    Record(
                        id = null,
                        tag = "개인시간",
                        startTime = LocalDateTime.now(),
                        endTime = null,
                        progressTime = 0,
                        pause = true
                    ))
                _selectedTagFlow.value = Tag(null, 5, "개인시간")
            }
        }
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

    fun insertRecord(tag : Tag){
        viewModelScope.launch(Dispatchers.IO) {
            recordUsecase.insertRecord(
                Record(
                    id = null,
                    tag = tag.title,
                    startTime = LocalDateTime.now(),
                    endTime = null,
                    progressTime = 0,
                    pause = true
            ))
        }
    }



    fun updateSchedule(schedule: Schedule){
        viewModelScope.launch(Dispatchers.IO) {
            scheduleUsecase.insertSchedule(schedule)
        }
    }

    fun deleteSchedule(schedule: Schedule){
        viewModelScope.launch(Dispatchers.IO) {
            scheduleUsecase.deleteSchedule(schedule)
        }
    }

    fun deleteSelectTag(tag: Tag){
        viewModelScope.launch (Dispatchers.IO) {
            tagUsecase.deleteTag(tag)
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
                    id = uiState.value.recordList[0].id!!,
                    pause = false
                )
            }
        }
    }

    fun initTagImages(tagImage : TypedArray){
        tagImages = tagImage
    }

    fun initScheduleImages(scheduleImage : TypedArray){
        scheduleImages = scheduleImage
    }

    fun getTagImages() = tagImages

    fun getScheduleImages() = scheduleImages

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