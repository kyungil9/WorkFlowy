package com.beank.data.repositoryimpl

import android.util.Log
import com.beank.domain.repository.LogRepository
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject

class LogRepositoryImpl @Inject constructor(
    private val crash : FirebaseCrashlytics
) : LogRepository {
    override fun logNonFatalCrash(throwable: Throwable) {
        crash.recordException(throwable)
    }

    override fun logCrash(message: String) {
        crash.log(message)
    }
}