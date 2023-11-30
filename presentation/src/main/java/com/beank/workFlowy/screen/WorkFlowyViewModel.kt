package com.beank.workFlowy.screen

import androidx.lifecycle.ViewModel
import com.beank.domain.repository.LogRepository
import com.beank.workFlowy.component.snackbar.SnackbarManager
import com.beank.workFlowy.component.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import com.google.firebase.FirebaseException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren

abstract class WorkFlowyViewModel(private val logRepository : LogRepository) : ViewModel() {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _,throwable ->
        SnackbarManager.showMessage(throwable.toSnackbarMessage())
        logRepository.logNonFatalCrash(throwable)
    }
    protected val ioContext = Dispatchers.IO + coroutineExceptionHandler
    private val jobContext = SupervisorJob() + coroutineExceptionHandler

    protected val mainScope = CoroutineScope(jobContext + Dispatchers.Main.immediate)
    protected val ioScope = CoroutineScope(jobContext + Dispatchers.IO)

    override fun onCleared() {
        jobContext.cancelChildren()
        ioContext.cancelChildren()
    }

    fun logFirebaseFatalCrash(message: String,e: FirebaseException){
        logRepository.logCrash(message)
        logRepository.logNonFatalCrash(e.fillInStackTrace())
    }
}