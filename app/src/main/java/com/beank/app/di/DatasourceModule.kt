package com.beank.app.di

import com.beank.data.datasource.SettingDataSource
import com.beank.data.datasource.StorageDataSource
import com.beank.data.datasource.impl.SettingDataSourceImpl
import com.beank.data.datasource.impl.StorageDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DatasourceModule {

    @Singleton
    @Binds
    abstract fun bindsStorageDatasource(Impl : StorageDataSourceImpl) : StorageDataSource

    @Singleton
    @Binds
    abstract fun bindsSettingDatasource(impl : SettingDataSourceImpl) : SettingDataSource

}