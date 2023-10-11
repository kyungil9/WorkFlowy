package com.beank.data.datasource

import com.beank.workFlowy.Setting
import kotlinx.coroutines.flow.Flow

interface SettingDataSource {

    fun getSettingState() : Flow<Setting>

    suspend fun updateDarkTheme(state : Boolean)

    suspend fun updateDynamicTheme(state : Boolean)

    suspend fun updateNoticeAlarm(state : Boolean)

    suspend fun updateScheduleAlarm(state : Boolean)

    suspend fun updateTrigger(state: Boolean)
}