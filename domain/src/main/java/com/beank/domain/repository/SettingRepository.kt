package com.beank.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingRepository {

    fun getDarkTheme() : Flow<Boolean>
    fun getDynamicTheme() : Flow<Boolean>
    fun getNoticeAlarm() : Flow<Boolean>
    fun getScheduleAlarm() : Flow<Boolean>

    suspend fun updateDarkTheme(state : Boolean)
    suspend fun updateDynamicTheme(state : Boolean)
    suspend fun updateNoticeAlarm(state : Boolean)
    suspend fun updateScheduleAlarm(state : Boolean)
}