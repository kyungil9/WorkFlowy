package com.beank.workFlowy.screen.analysis

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.beank.domain.model.Record
import com.beank.domain.model.onEmpty
import com.beank.domain.model.onException
import com.beank.domain.model.onLoading
import com.beank.domain.model.onSuccess
import com.beank.domain.repository.LogRepository
import com.beank.domain.usecase.AnalysisUsecases
import com.beank.presentation.R
import com.beank.workFlowy.component.snackbar.SnackbarManager
import com.beank.workFlowy.screen.WorkFlowyViewModel
import com.beank.workFlowy.screen.analysis.Date.DAY
import com.beank.workFlowy.screen.analysis.Date.MONTH
import com.beank.workFlowy.screen.analysis.Date.WEEK
import com.beank.workFlowy.screen.analysis.Date.YEAR
import com.beank.workFlowy.utils.changeDayInfo
import com.beank.workFlowy.utils.toWeekEnd
import com.beank.workFlowy.utils.toWeekStart
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@Stable
data class AnalysisUiState(
    val recordList : List<Record> = emptyList()
)

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class AnalysisViewModel @Inject constructor(
    private val analysisUsecases: AnalysisUsecases,
    logRepository: LogRepository
) : WorkFlowyViewModel(logRepository){

    private val _selectDayFlow = MutableStateFlow<LocalDate>(LocalDate.now())
    private val _toggleButtonFlow = MutableStateFlow(0)
    private val _animateStackChannel = Channel<Boolean>(Channel.BUFFERED)
    private var todayJob : Job? = null
    val selectDayFlow get() = _selectDayFlow.asStateFlow()
    val toggleButtonFlow get() = _toggleButtonFlow.asStateFlow()
    val animateStackChannel get() = _animateStackChannel.receiveAsFlow()

    var actBoxProgressFlow by mutableStateOf(true)
        private set
    var uiState by mutableStateOf(AnalysisUiState())
        private set

    init {
        getPeriodRecord()
    }

    fun updateToggleButton(){
        _toggleButtonFlow.value = (toggleButtonFlow.value+1)%4
        sendAnimationEvent(false)
    }

    fun changeSelectDay(day : LocalDate){
        _selectDayFlow.value = day
        sendAnimationEvent(false)

    }

    fun sendAnimationEvent(value: Boolean){
        viewModelScope.launch {
            _animateStackChannel.send(value)
        }
    }

    fun rightDragDate(){
        when(toggleButtonFlow.value){
            DAY -> _selectDayFlow.value = selectDayFlow.value.plusDays(1)
            WEEK -> _selectDayFlow.value = selectDayFlow.value.plusWeeks(1)
            MONTH -> _selectDayFlow.value = selectDayFlow.value.plusMonths(1)
            YEAR -> _selectDayFlow.value = selectDayFlow.value.plusYears(1)
        }
    }

    fun leftDragDate(){
        when(toggleButtonFlow.value){
            DAY -> _selectDayFlow.value = selectDayFlow.value.minusDays(1)
            WEEK -> _selectDayFlow.value = selectDayFlow.value.minusWeeks(1)
            MONTH -> _selectDayFlow.value = selectDayFlow.value.minusMonths(1)
            YEAR -> _selectDayFlow.value = selectDayFlow.value.minusYears(1)
        }
    }

    private fun getPeriodRecord() {
        launchCatching {
            toggleButtonFlow.combine(selectDayFlow){toggle,date -> PeriodDate(date, toggle)}.collectLatest { period ->
                todayJob?.cancel()
                todayJob = analysisUsecases.getPeriodRecord(period.startDate(),period.endDate())
                    .flowOn(Dispatchers.IO).cancellable().onEach { state ->
                        state.onEmpty {
                            uiState = uiState.copy(recordList = emptyList())
                            actBoxProgressFlow = false
                        }
                        state.onLoading {
                            actBoxProgressFlow = true
                        }
                        state.onSuccess { recordList ->
                            actBoxProgressFlow = false
                            val totalRecord = ArrayList<Record>()
                            recordList.map { record ->
                                val findRecordIndex = totalRecord.indexOfFirst { it.tag == record.tag }
                                if ( findRecordIndex == -1){
                                    totalRecord.add(record)
                                }else{
                                    totalRecord[findRecordIndex] = totalRecord[findRecordIndex].copy(progressTime = totalRecord[findRecordIndex].progressTime + record.progressTime)
                                }
                            }
                            totalRecord.sortByDescending {it.progressTime}
                            uiState = uiState.copy(recordList = totalRecord)
                        }
                        state.onException { message, e ->
                            SnackbarManager.showMessage(R.string.firebase_server_error)
                            logFirebaseFatalCrash(message,e)
                        }
                    }.launchIn(viewModelScope)
            }
        }
    }
}

data class PeriodDate(
    val date: LocalDate,
    val toggle : Int
){
    @RequiresApi(Build.VERSION_CODES.O)
    fun startDate() : LocalDate = when(toggle){
        DAY -> date
        WEEK -> date.toWeekStart()
        MONTH -> LocalDate.of(date.year,date.monthValue,1)
        YEAR -> LocalDate.of(date.year,1,1)
        else -> date
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun endDate() : LocalDate = when(toggle){
        DAY -> date
        WEEK -> date.toWeekEnd()
        MONTH -> LocalDate.of(date.year,date.monthValue, changeDayInfo(date))
        YEAR -> LocalDate.of(date.year,12,31)
        else -> date
    }
}

object Date{
    const val DAY = 0
    const val WEEK = 1
    const val MONTH = 2
    const val YEAR = 3
}