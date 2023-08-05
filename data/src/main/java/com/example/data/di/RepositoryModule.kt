package com.example.data.di

import com.example.data.datasource.local.RecordDatasource
import com.example.data.datasource.local.ScheduleDatasource
import com.example.data.datasource.local.TagDatasource
import com.example.data.repositoryimpl.RecordRepositoryImpl
import com.example.data.repositoryimpl.ScheduleRepositoryImpl
import com.example.data.repositoryimpl.TagRepositoryImpl
import com.example.domain.repository.RecordRepository
import com.example.domain.repository.ScheduleRepository
import com.example.domain.repository.TagRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object RepositoryModule {

    @Singleton
    @Provides
    fun bindsRecordRepository(recordDatasource: RecordDatasource) : RecordRepository {
        return RecordRepositoryImpl(recordDatasource)
    }

    @Singleton
    @Provides
    fun bindsScheduleRepository(scheduleDatasource: ScheduleDatasource) : ScheduleRepository {
        return ScheduleRepositoryImpl(scheduleDatasource)
    }

    @Singleton
    @Provides
    fun bindsTagRepository(tagDatasource: TagDatasource) : TagRepository {
        return TagRepositoryImpl(tagDatasource)
    }

}