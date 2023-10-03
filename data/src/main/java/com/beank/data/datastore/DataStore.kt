package com.beank.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.beank.workFlowy.Setting

private const val DATA_STORE_NAME = "setting.pb"

val Context.setting : DataStore<Setting> by dataStore(
    fileName = DATA_STORE_NAME,
    serializer = SettingSerializer
)