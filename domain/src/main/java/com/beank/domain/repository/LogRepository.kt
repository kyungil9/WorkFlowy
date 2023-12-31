package com.beank.domain.repository

interface LogRepository {
    fun logNonFatalCrash(throwable: Throwable)

    fun logCrash(message : String)
}