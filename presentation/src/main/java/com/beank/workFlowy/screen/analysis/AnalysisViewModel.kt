package com.beank.workFlowy.screen.analysis

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.beank.workFlowy.utils.toEndTimeLong
import com.beank.workFlowy.utils.toStartTimeLong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
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

@HiltViewModel
class AnalysisViewModel @Inject constructor(
    private val analysisUsecases: AnalysisUsecases,
    logRepository: LogRepository
) : WorkFlowyViewModel(logRepository){

    private val _selectDayFlow = MutableStateFlow<LocalDate>(LocalDate.now())
    private val _toggleButtonFlow = MutableStateFlow(0)
    private val _animateStackChannel = Channel<Boolean>(Channel.BUFFERED)
    private var todayJob : Job? = null
    private var create = false
    val selectDayFlow get() = _selectDayFlow.asStateFlow()
    val toggleButtonFlow get() = _toggleButtonFlow.asStateFlow()
    val animateStackChannel get() = _animateStackChannel.receiveAsFlow()

    var actBoxProgressFlow by mutableStateOf(true)
        private set
    var uiState by mutableStateOf(AnalysisUiState())
        private set
    var animateStack by mutableStateOf(false)
        private set


    init {
        getTodayRecord()
    }

    fun updateAnimateStack(value : Boolean){
        animateStack = value
    }

    fun updateToggleButton(){
        _toggleButtonFlow.value = (toggleButtonFlow.value+1)%4
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

    //애니메이션 처리 어떻게 할지 고민?
    private fun getTodayRecord() {
        launchCatching {
            selectDayFlow.collectLatest {
                todayJob?.cancel()
                todayJob = analysisUsecases.getTodayRecord(it)
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
                            uiState = uiState.copy(recordList = recordList)
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