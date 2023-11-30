package com.beank.workFlowy.screen.trigger

import com.beank.domain.model.onEmpty
import com.beank.domain.model.onException
import com.beank.domain.model.onLoading
import com.beank.domain.model.onSuccess
import com.beank.domain.repository.LogRepository
import com.beank.domain.usecase.TriggerUsecases
import com.beank.presentation.R
import com.beank.workFlowy.component.snackbar.SnackbarManager
import com.beank.workFlowy.screen.WorkFlowyViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TriggerViewModel @Inject constructor(
    private val triggerUsecases: TriggerUsecases,
    logRepository: LogRepository
) : WorkFlowyViewModel(logRepository){

    val uiState = TriggerUiState()

    init {
        getAllTriggerList()
    }

    fun onTriggerRemove(id : String){
        ioScope.launch {
            triggerUsecases.removeGeofence(id)
        }
    }

    private fun getAllTriggerList() = triggerUsecases.getGeoTriggerList()
        .flowOn(ioContext).onEach { state ->
            state.onEmpty {
                uiState.progressToggle = false
                uiState.triggerList = emptyList()
            }
            state.onLoading {
                uiState.progressToggle = true
            }
            state.onSuccess {geoList ->
                uiState.progressToggle = false
                uiState.triggerList = geoList
            }
            state.onException { message, e ->
                uiState.progressToggle = false
                SnackbarManager.showMessage(R.string.firebase_server_error)
                logFirebaseFatalCrash(message, e)
            }
        }.launchIn(mainScope)


}