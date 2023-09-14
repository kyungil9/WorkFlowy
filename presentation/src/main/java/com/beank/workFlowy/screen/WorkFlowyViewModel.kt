package com.beank.workFlowy.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beank.domain.service.LogService
import com.beank.workFlowy.component.snackbar.SnackbarManager
import com.beank.workFlowy.component.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class WorkFlowyViewModel(private val logService : LogService) : ViewModel() {
    fun launchCatching(snackbar : Boolean = true, block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch (
            CoroutineExceptionHandler{ _, throwable ->
                if (snackbar)
                    SnackbarManager.showMessage(throwable.toSnackbarMessage())
                logService.logNonFatalCrash(throwable)
            },
            block = block
        )
}