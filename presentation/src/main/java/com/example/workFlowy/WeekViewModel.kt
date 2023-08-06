package com.example.workFlowy

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.R
import com.example.data.datasource.local.database.entity.WeekRecord
import com.example.data.datasource.local.database.entity.WeekSchedule
import com.example.data.datasource.local.database.entity.WeekTag
import com.example.domain.model.Record
import com.example.domain.model.Schedule
import com.example.domain.model.Tag
import com.example.domain.usecase.RecordUsecase
import com.example.domain.usecase.ScheduleUsecase
import com.example.domain.usecase.TagUsecase
import com.example.workFlowy.utils.today
import com.example.workFlowy.utils.transDayToKorean
import com.google.android.material.bottomsheet.BottomSheetBehavior.StableState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
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

    private val _uiState = MutableStateFlow(WeekUiState())
    private val _selectDayFlow = MutableStateFlow<LocalDate>(LocalDate.now())
    val selectDayFlow get() = _selectDayFlow.asStateFlow()
    val uiState get() = _uiState.asStateFlow()
    val selectDayStringFlow get() = _selectDayFlow.asStateFlow().map { "< ${it.year%100}/${it.monthValue}/${it.dayOfMonth} ${transDayToKorean(it.dayOfWeek.value)} >" }

    init {
        initTag()
        getAllTagInfo()
        getSeleteSchedule(selectDayFlow.value)
    }

    fun changeSelectDay(day : LocalDate){
        _selectDayFlow.value = day
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

    private fun getAllTagInfo() = tagUsecase.getTagInfo()
        .onEach { tagList ->
            _uiState.update { state -> state.copy(tagList = tagList) }
        }.launchIn(viewModelScope)

    private fun getSeleteSchedule(selectDay : LocalDate) = scheduleUsecase.getScheduleInfo(selectDay)
        .onEach { scheduleList ->
            _uiState.update { state -> state.copy(scheduleList = scheduleList) }
        }.launchIn(viewModelScope)



}