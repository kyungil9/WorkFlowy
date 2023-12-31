package com.beank.app.di

import com.beank.data.repositoryimpl.AccountRepositoryImpl
import com.beank.data.repositoryimpl.GeofenceRepositoryImpl
import com.beank.data.repositoryimpl.LogRepositoryImpl
import com.beank.data.repositoryimpl.MessageRepositoryImpl
import com.beank.data.repositoryimpl.RecordRepositoryImpl
import com.beank.data.repositoryimpl.ScheduleRepositoryImpl
import com.beank.data.repositoryimpl.SettingRepositoryImpl
import com.beank.data.repositoryimpl.TagRepositoryImpl
import com.beank.data.repositoryimpl.UserRepositoryImpl
import com.beank.domain.repository.AccountRepository
import com.beank.domain.repository.GeofenceRepository
import com.beank.domain.repository.LogRepository
import com.beank.domain.repository.MessageRepository
import com.beank.domain.repository.RecordRepository
import com.beank.domain.repository.ScheduleRepository
import com.beank.domain.repository.SettingRepository
import com.beank.domain.repository.TagRepository
import com.beank.domain.repository.UserRepository
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

    @Singleton
    @Binds
    abstract fun bindsMessageRepository(impl : MessageRepositoryImpl) : MessageRepository

    @Singleton
    @Binds
    abstract fun bindsSettingRepository(impl : SettingRepositoryImpl) : SettingRepository

    @Singleton
    @Binds
    abstract fun bindsUserRepository(impl : UserRepositoryImpl) : UserRepository

    @Singleton
    @Binds
    abstract fun bindsGeofenceRepository(impl : GeofenceRepositoryImpl) : GeofenceRepository

}