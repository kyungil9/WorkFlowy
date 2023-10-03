package com.beank.app.di

import android.content.Context
import androidx.datastore.core.DataStore
import com.beank.data.datastore.setting
import com.beank.workFlowy.Setting
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Singleton
    @Provides
    fun providesSettingDataStore(@ApplicationContext context: Context) : DataStore<Setting>{
        return context.setting
    }
}