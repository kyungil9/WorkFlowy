package com.beank.data.datasource.impl

import androidx.datastore.core.DataStore
import com.beank.data.datasource.SettingDataSource
import com.beank.workFlowy.Setting
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingDataSourceImpl @Inject constructor(
    private val settingDataStore: DataStore<Setting>
) : SettingDataSource {

    override fun getSettingState(): Flow<Setting> = settingDataStore.data

    override suspend fun updateDarkTheme(state: Boolean) {
        settingDataStore.updateData { setting ->
            setting.toBuilder()
                .setDarkTheme(state)
                .build()
        }
    }

    override suspend fun updateDynamicTheme(state: Boolean) {
        settingDataStore.updateData { setting ->
            setting.toBuilder()
                .setDynamicTheme(state)
                .build()
        }
    }

    override suspend fun updateNoticeAlarm(state: Boolean) {
        settingDataStore.updateData { setting ->
            setting.toBuilder()
                .setNoticeAlarm(state)
                .build()
        }
    }

    override suspend fun updateScheduleAlarm(state: Boolean) {
        settingDataStore.updateData { setting ->
            setting.toBuilder()
                .setScheduleAlarm(state)
                .build()
        }
    }

}