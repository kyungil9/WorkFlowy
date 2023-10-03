package com.beank.app.di

import com.beank.domain.repository.AccountRepository
import com.beank.domain.repository.MessageRepository
import com.beank.domain.repository.RecordRepository
import com.beank.domain.repository.ScheduleRepository
import com.beank.domain.repository.SettingRepository
import com.beank.domain.repository.TagRepository
import com.beank.domain.usecase.AnalysisUsecases
import com.beank.domain.usecase.LoginUsecases
import com.beank.domain.usecase.ScheduleUsecases
import com.beank.domain.usecase.SettingUsecases
import com.beank.domain.usecase.SignUpUsecases
import com.beank.domain.usecase.TagUsecases
import com.beank.domain.usecase.WeekUsecases
import com.beank.domain.usecase.account.CreateAccount
import com.beank.domain.usecase.account.LoginAccount
import com.beank.domain.usecase.message.InsertToken
import com.beank.domain.usecase.record.GetNowRecord
import com.beank.domain.usecase.record.GetPeriodRecord
import com.beank.domain.usecase.record.InsertRecord
import com.beank.domain.usecase.record.StartNewRecord
import com.beank.domain.usecase.record.UpdateRecord
import com.beank.domain.usecase.schedule.DeleteSchedule
import com.beank.domain.usecase.schedule.GetTodaySchedule
import com.beank.domain.usecase.schedule.InsertSchedule
import com.beank.domain.usecase.schedule.UpdateCheckSchedule
import com.beank.domain.usecase.schedule.UpdateSchedule
import com.beank.domain.usecase.setting.GetDarkTheme
import com.beank.domain.usecase.setting.GetDynamicTheme
import com.beank.domain.usecase.setting.GetNoticeAlarm
import com.beank.domain.usecase.setting.GetScheduleAlarm
import com.beank.domain.usecase.setting.UpdateDarkTheme
import com.beank.domain.usecase.setting.UpdateDynamicTheme
import com.beank.domain.usecase.setting.UpdateNoticeAlarm
import com.beank.domain.usecase.setting.UpdateScheduleAlarm
import com.beank.domain.usecase.tag.CheckTagTitle
import com.beank.domain.usecase.tag.DeleteTag
import com.beank.domain.usecase.tag.GetAllTag
import com.beank.domain.usecase.tag.InitDataSetting
import com.beank.domain.usecase.tag.InsertTag
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UsecaseModule {

    @Provides
    @Singleton
    fun provideWeekUseCases(recordRepository: RecordRepository,scheduleRepository: ScheduleRepository,tagRepository: TagRepository) = WeekUsecases(
        getAllTag = GetAllTag(tagRepository),
        getTodaySchedule = GetTodaySchedule(scheduleRepository),
        getNowRecord = GetNowRecord(recordRepository),
        startNewRecord = StartNewRecord(recordRepository),
        deleteTag = DeleteTag(tagRepository),
        deleteSchedule = DeleteSchedule(scheduleRepository),
        updateCheckSchedule = UpdateCheckSchedule(scheduleRepository),
        updateRecord = UpdateRecord(recordRepository),
        insertRecord = InsertRecord(recordRepository)
    )


    @Provides
    @Singleton
    fun provideLoginUseCases(accountRepository: AccountRepository,tagRepository: TagRepository,recordRepository: RecordRepository,messageRepository: MessageRepository) = LoginUsecases(
        loginAccount = LoginAccount(accountRepository),
        initDataSetting = InitDataSetting(
            InsertRecord(recordRepository),
            InsertTag(tagRepository)
        ),
        insertToken = InsertToken(messageRepository)
    )


    @Provides
    @Singleton
    fun provideSingUpUseCases(accountRepository: AccountRepository,tagRepository: TagRepository,recordRepository: RecordRepository) = SignUpUsecases(
        createAccount = CreateAccount(accountRepository),
        initDataSetting = InitDataSetting(
            InsertRecord(recordRepository),
            InsertTag(tagRepository)
        )
    )


    @Provides
    @Singleton
    fun provideScheduleUseCases(scheduleRepository: ScheduleRepository) = ScheduleUsecases(
        insertSchedule = InsertSchedule(scheduleRepository),
        updateSchedule = UpdateSchedule(scheduleRepository)
    )

    @Provides
    @Singleton
    fun provideTagUseCases(tagRepository: TagRepository) = TagUsecases(
        checkTagTitle = CheckTagTitle(tagRepository),
        insertTag = InsertTag(tagRepository)
    )

    @Provides
    @Singleton
    fun provideAnalysisUseCases(recordRepository: RecordRepository) = AnalysisUsecases(
        getPeriodRecord = GetPeriodRecord(recordRepository)
    )

    @Provides
    @Singleton
    fun provideSettingUseCases(settingRepository: SettingRepository) = SettingUsecases(
        getDarkTheme = GetDarkTheme(settingRepository),
        getDynamicTheme = GetDynamicTheme(settingRepository),
        getNoticeAlarm = GetNoticeAlarm(settingRepository),
        getScheduleAlarm = GetScheduleAlarm(settingRepository),
        updateDarkTheme = UpdateDarkTheme(settingRepository),
        updateDynamicTheme = UpdateDynamicTheme(settingRepository),
        updateNoticeAlarm = UpdateNoticeAlarm(settingRepository),
        updateScheduleAlarm = UpdateScheduleAlarm(settingRepository)
    )

}