package com.beank.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingRepository {

    fun getDarkTheme() : Flow<Boolean>
    fun getDynamicTheme() : Flow<Boolean>
    fun getNoticeAlarm() : Flow<Boolean>
    fun getScheduleAlarm() : Flow<Boolean>
    fun getTriggerToggle() : Flow<Boolean>
    fun getMoveTriggerToggle() : Flow<Boolean>
    fun getGeoState() : Flow<Boolean>
    fun getMoveState() : Flow<Boolean>
    fun getRecordAlarm() : Flow<Boolean>
    fun getTimePause() : Flow<Boolean>

    suspend fun getScheduleState() : Boolean
    suspend fun getNoticeState() : Boolean

    suspend fun updateDarkTheme(state : Boolean)
    suspend fun updateDynamicTheme(state : Boolean)
    suspend fun updateNoticeAlarm(state : Boolean)
    suspend fun updateScheduleAlarm(state : Boolean)
    suspend fun updateTriggerToggle(state: Boolean)
    suspend fun updateMoveTriggerToggle(state: Boolean)
    suspend fun updateGeoState(state: Boolean)
    suspend fun updateMoveState(state: Boolean)
    suspend fun updateRecordAlarm(state: Boolean)
    suspend fun updateTimePause(state: Boolean)

    suspend fun initSetting(value : Boolean)

}