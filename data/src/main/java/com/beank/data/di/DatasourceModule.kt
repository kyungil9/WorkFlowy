package com.beank.data.di

import com.beank.data.datasource.local.RecordDatasource
import com.beank.data.datasource.local.ScheduleDatasource
import com.beank.data.datasource.local.TagDatasource
import com.beank.data.datasource.local.impl.RecordDatasourceImpl
import com.beank.data.datasource.local.impl.ScheduleDatasourceImpl
import com.beank.data.datasource.local.impl.TagDatasourceImpl
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