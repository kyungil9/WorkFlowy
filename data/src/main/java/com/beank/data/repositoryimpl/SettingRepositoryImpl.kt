package com.beank.data.repositoryimpl

import com.beank.data.datasource.SettingDataSource
import com.beank.domain.repository.SettingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingRepositoryImpl @Inject constructor(
    private val settingDataSource: SettingDataSource
) : SettingRepository {

    override fun getDarkTheme(): Flow<Boolean> = settingDataSource.getSettingState().map {it.darkTheme}

    override fun getDynamicTheme(): Flow<Boolean> = settingDataSource.getSettingState().map { it.dynamicTheme }

    override  fun getNoticeAlarm(): Flow<Boolean> = settingDataSource.getSettingState().map { it.noticeAlarm }

    override fun getScheduleAlarm(): Flow<Boolean> = settingDataSource.getSettingState().map { it.scheduleAlarm }

    override fun getTriggerToggle(): Flow<Boolean> = settingDataSource.getSettingState().map { it.trigger }

    override fun getMoveTriggerToggle(): Flow<Boolean> = settingDataSource.getSettingState().map { it.moveTrigger }

    override fun getGeoState(): Flow<Boolean> = settingDataSource.getSettingState().map { it.geo }

    override fun getMoveState(): Flow<Boolean> = settingDataSource.getSettingState().map { it.move }

    override fun getRecordAlarm(): Flow<Boolean> = settingDataSource.getSettingState().map { it.recordAlarm }

    override suspend fun getNoticeState(): Boolean = settingDataSource.getSettingState().first().noticeAlarm

    override suspend fun getScheduleState(): Boolean = settingDataSource.getSettingState().first().scheduleAlarm


    override suspend fun updateDarkTheme(state : Boolean) : Unit = settingDataSource.updateDarkTheme(state)

    override suspend fun updateDynamicTheme(state : Boolean) : Unit = settingDataSource.updateDynamicTheme(state)

    override suspend fun updateNoticeAlarm(state : Boolean) : Unit = settingDataSource.updateNoticeAlarm(state)

    override suspend fun updateScheduleAlarm(state : Boolean) : Unit = settingDataSource.updateScheduleAlarm(state)

    override suspend fun updateTriggerToggle(state: Boolean) : Unit = settingDataSource.updateTrigger(state)

    override suspend fun updateMoveTriggerToggle(state: Boolean) : Unit = settingDataSource.updateMoveTrigger(state)

    override suspend fun updateGeoState(state: Boolean) : Unit = settingDataSource.updateGeoState(state)

    override suspend fun updateMoveState(state: Boolean) : Unit = settingDataSource.updateMoveState(state)

    override suspend fun updateRecordAlarm(state: Boolean) : Unit = settingDataSource.updateRecordAlarm(state)

    override suspend fun initSetting(value : Boolean) {
        settingDataSource.updateDarkTheme(false)
        settingDataSource.updateDynamicTheme(false)
        settingDataSource.updateNoticeAlarm(value)
        settingDataSource.updateScheduleAlarm(value)
        settingDataSource.updateTrigger(false)
        settingDataSource.updateMoveTrigger(false)
        settingDataSource.updateGeoState(false)
        settingDataSource.updateMoveState(false)
        settingDataSource.updateRecordAlarm(false)
    }

}