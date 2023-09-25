package com.beank.app.di

import com.beank.data.repositoryimpl.AccountRepositoryImpl
import com.beank.data.repositoryimpl.LogRepositoryImpl
import com.beank.data.repositoryimpl.RecordRepositoryImpl
import com.beank.data.repositoryimpl.ScheduleRepositoryImpl
import com.beank.data.repositoryimpl.TagRepositoryImpl
import com.beank.domain.repository.AccountRepository
import com.beank.domain.repository.LogRepository
import com.beank.domain.repository.RecordRepository
import com.beank.domain.repository.ScheduleRepository
import com.beank.domain.repository.TagRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindsLogRepository(impl : LogRepositoryImpl) : LogRepository

    @Singleton
    @Binds
    abstract fun bindsAccountRepository(impl : AccountRepositoryImpl) : AccountRepository

    @Singleton
    @Binds
    abstract fun bindsRecordRepository(impl : RecordRepositoryImpl) : RecordRepository

    @Singleton
    @Binds
    abstract fun bindsScheduleRepository(impl : ScheduleRepositoryImpl) : ScheduleRepository

    @Singleton
    @Binds
    abstract fun bindsTagRepository(impl : TagRepositoryImpl) : TagRepository

}