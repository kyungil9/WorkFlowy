package com.beank.workFlowy.di

import com.beank.domain.usecase.RecordUsecase
import com.beank.domain.usecase.ScheduleUsecase
import com.beank.domain.usecase.TagUsecase
import com.beank.domain.usecase.impl.RecordUsecaseImpl
import com.beank.domain.usecase.impl.ScheduleUsecaseImpl
import com.beank.domain.usecase.impl.TagUsecaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UsecaseModule{

    @Singleton
    @Binds
    abstract fun bindsRecordUsecase(recordUsecaseImpl: RecordUsecaseImpl) : RecordUsecase

    @Singleton
    @Binds
    abstract fun bindsScheduleUsecase(scheduleUsecaseImpl: ScheduleUsecaseImpl) : ScheduleUsecase

    @Singleton
    @Binds
    abstract fun bindsTagUsecase(tagUsecaseImpl: TagUsecaseImpl) : TagUsecase
}