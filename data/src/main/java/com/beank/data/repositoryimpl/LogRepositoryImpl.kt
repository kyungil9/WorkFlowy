package com.beank.data.repositoryimpl

import com.beank.domain.repository.LogRepository
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject

class LogRepositoryImpl @Inject constructor(
    private val crash : FirebaseCrashlytics
) : LogRepository {
    override fun logNonFatalCrash(throwable: Throwable) {
        crash.recordException(throwable)
    }
}