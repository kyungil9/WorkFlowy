package com.example.data.di

import com.example.data.datasource.local.RecordDatasource
import com.example.data.datasource.local.ScheduleDatasource
import com.example.data.datasource.local.TagDatasource
import com.example.data.datasource.local.impl.RecordDatasourceImpl
import com.example.data.datasource.local.impl.ScheduleDatasourceImpl
import com.example.data.datasource.local.impl.TagDatasourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DatasourceModule {

    @Singleton
    @Binds
    abstract fun bindsRecordDatasource(recordDatasourceImpl : RecordDatasourceImpl) : RecordDatasource

    @Singleton
    @Binds
    abstract fun bindsTagDatasource(tagDatasourceImpl : TagDatasourceImpl) : TagDatasource

    @Singleton
    @Binds
    abstract fun bindsScheduleDatasource(scheduleDatasourceImpl : ScheduleDatasourceImpl) : ScheduleDatasource
}