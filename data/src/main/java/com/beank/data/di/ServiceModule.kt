package com.beank.data.di

import com.beank.data.serviceimpl.AccountServiceImpl
import com.beank.data.serviceimpl.LogServiceImpl
import com.beank.domain.service.AccountService
import com.beank.domain.service.LogService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {

    @Binds
    abstract fun provideLogService(impl : LogServiceImpl) : LogService

    @Binds
    abstract fun provideAccountService(impl : AccountServiceImpl) : AccountService
}