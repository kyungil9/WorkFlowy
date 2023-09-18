package com.beank.workFlowy.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beank.domain.repository.LogRepository
import com.beank.workFlowy.component.snackbar.SnackbarManager
import com.beank.workFlowy.component.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class WorkFlowyViewModel(private val logRepository : LogRepository) : ViewModel() {
    fun launchCatching(snackbar : Boolean = true, block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch (
            CoroutineExceptionHandler{ _, throwable ->
                if (snackbar)
                    SnackbarManager.showMessage(throwable.toSnackbarMessage())
                logRepository.logNonFatalCrash(throwable)
            },
            block = block
        )
}