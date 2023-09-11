package com.beank.data.di

import com.beank.data.datasource.local.RecordDatasource
import com.beank.data.datasource.local.ScheduleDatasource
import com.beank.data.datasource.local.TagDatasource
import com.beank.data.repositoryimpl.RecordRepositoryImpl
import com.beank.data.repositoryimpl.ScheduleRepositoryImpl
import com.beank.data.repositoryimpl.TagRepositoryImpl
import com.beank.domain.repository.RecordRepository
import com.beank.domain.repository.ScheduleRepository
import com.beank.domain.repository.TagRepository
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