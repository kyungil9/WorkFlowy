package com.beank.domain.service

interface LogService {
    fun logNonFatalCrash(throwable: Throwable)
}