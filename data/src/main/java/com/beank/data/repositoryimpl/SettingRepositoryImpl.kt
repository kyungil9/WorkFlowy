package com.beank.data.repositoryimpl

import com.beank.data.datasource.SettingDataSource
import com.beank.domain.repository.SettingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingRepositoryImpl @Inject constructor(
    private val settingDataSource: SettingDataSource
) : SettingRepository {

    override fun getDarkTheme(): Flow<Boolean> = settingDataSource.getSettingState().map {it.darkTheme}

    override fun getDynamicTheme(): Flow<Boolean> = settingDataSource.getSettingState().map { it.dynamicTheme }

    override fun getNoticeAlarm(): Flow<Boolean> = settingDataSource.getSettingState().map { it.noticeAlarm }

    override fun getScheduleAlarm(): Flow<Boolean> = settingDataSource.getSettingState().map { it.scheduleAlarm }

    override suspend fun updateDarkTheme(state : Boolean) : Unit = settingDataSource.updateDarkTheme(state)

    override suspend fun updateDynamicTheme(state : Boolean) : Unit = settingDataSource.updateDynamicTheme(state)

    override suspend fun updateNoticeAlarm(state : Boolean) : Unit = settingDataSource.updateNoticeAlarm(state)

    override suspend fun updateScheduleAlarm(state : Boolean) : Unit = settingDataSource.updateScheduleAlarm(state)
}