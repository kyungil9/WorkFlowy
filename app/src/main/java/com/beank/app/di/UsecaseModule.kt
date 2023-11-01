package com.beank.app.di

import com.beank.domain.repository.AccountRepository
import com.beank.domain.repository.GeofenceRepository
import com.beank.domain.repository.MessageRepository
import com.beank.domain.repository.RecordRepository
import com.beank.domain.repository.ScheduleRepository
import com.beank.domain.repository.SettingRepository
import com.beank.domain.repository.TagRepository
import com.beank.domain.repository.UserRepository
import com.beank.domain.usecase.AlarmUsecases
import com.beank.domain.usecase.AnalysisUsecases
import com.beank.domain.usecase.GeoUsecases
import com.beank.domain.usecase.LoginUsecases
import com.beank.domain.usecase.RecordAlarmUsecases
import com.beank.domain.usecase.ScheduleUsecases
import com.beank.domain.usecase.SettingUsecases
import com.beank.domain.usecase.SignUpUsecases
import com.beank.domain.usecase.TagUsecases
import com.beank.domain.usecase.TriggerSettingUsecases
import com.beank.domain.usecase.TriggerUsecases
import com.beank.domain.usecase.UserUsecases
import com.beank.domain.usecase.WeekUsecases
import com.beank.domain.usecase.account.CreateAccount
import com.beank.domain.usecase.account.LoginAccount
import com.beank.domain.usecase.account.SignOut
import com.beank.domain.usecase.geo.AddGeofence
import com.beank.domain.usecase.geo.GetChooseGeofence
import com.beank.domain.usecase.geo.GetGeoTriggerList
import com.beank.domain.usecase.geo.GetTempGeoTrigger
import com.beank.domain.usecase.geo.RemoveGeofence
import com.beank.domain.usecase.geo.StartGeofenceToClient
import com.beank.domain.usecase.geo.UpdateGeofence
import com.beank.domain.usecase.message.InsertToken
import com.beank.domain.usecase.message.SubscribeNotice
import com.beank.domain.usecase.message.UnsubscribeNotice
import com.beank.domain.usecase.record.GetCurrentRecord
import com.beank.domain.usecase.record.GetNowRecord
import com.beank.domain.usecase.record.GetPeriodRecord
import com.beank.domain.usecase.record.InsertRecord
import com.beank.domain.usecase.record.StartNewRecord
import com.beank.domain.usecase.record.UpdateRecord
import com.beank.domain.usecase.schedule.DeleteSchedule
import com.beank.domain.usecase.schedule.GetAlarmSchedule
import com.beank.domain.usecase.schedule.GetTodaySchedule
import com.beank.domain.usecase.schedule.InsertSchedule
import com.beank.domain.usecase.schedule.UpdateCheckSchedule
import com.beank.domain.usecase.schedule.UpdateSchedule
import com.beank.domain.usecase.setting.GetDarkTheme
import com.beank.domain.usecase.setting.GetDynamicTheme
import com.beank.domain.usecase.setting.GetGeoState
import com.beank.domain.usecase.setting.GetMoveState
import com.beank.domain.usecase.setting.GetNoticeAlarm
import com.beank.domain.usecase.setting.GetNoticeState
import com.beank.domain.usecase.setting.GetRecordAlarm
import com.beank.domain.usecase.setting.GetScheduleAlarm
import com.beank.domain.usecase.setting.GetScheduleState
import com.beank.domain.usecase.setting.GetTriggerToggle
import com.beank.domain.usecase.setting.InitSetting
import com.beank.domain.usecase.setting.UpdateDarkTheme
import com.beank.domain.usecase.setting.UpdateDynamicTheme
import com.beank.domain.usecase.setting.UpdateGeoState
import com.beank.domain.usecase.setting.UpdateMoveState
import com.beank.domain.usecase.setting.UpdateNoticeAlarm
import com.beank.domain.usecase.setting.UpdateScheduleAlarm
import com.beank.domain.usecase.setting.UpdateTriggerToggle
import com.beank.domain.usecase.tag.CheckTagTitle
import com.beank.domain.usecase.tag.DeleteTag
import com.beank.domain.usecase.tag.GetAllTag
import com.beank.domain.usecase.tag.GetNextTag
import com.beank.domain.usecase.tag.InitDataSetting
import com.beank.domain.usecase.tag.InsertTag
import com.beank.domain.usecase.user.GetUserInfo
import com.beank.domain.usecase.user.InitUserInfo
import com.beank.domain.usecase.user.UpdateUserGrade
import com.beank.domain.usecase.user.UpdateUserNickName
import com.beank.domain.usecase.user.UploadImageUrl
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
    fun provideLoginUseCases(accountRepository: AccountRepository,tagRepository: TagRepository,recordRepository: RecordRepository, messageRepository: MessageRepository,userRepository: UserRepository,settingRepository: SettingRepository) = LoginUsecases(
        loginAccount = LoginAccount(accountRepository),
        initDataSetting = InitDataSetting(
            InsertRecord(recordRepository),
            InsertTag(tagRepository),
            InitUserInfo(userRepository)
        ),
        insertToken = InsertToken(messageRepository),
        initSetting = InitSetting(settingRepository),
        subscribeNotice = SubscribeNotice(messageRepository)
    )


    @Provides
    @Singleton
    fun provideSingUpUseCases(accountRepository: AccountRepository,tagRepository: TagRepository,recordRepository: RecordRepository, userRepository: UserRepository) = SignUpUsecases(
        createAccount = CreateAccount(accountRepository),
        initDataSetting = InitDataSetting(
            InsertRecord(recordRepository),
            InsertTag(tagRepository),
            InitUserInfo(userRepository)
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
    fun provideSettingUseCases(settingRepository: SettingRepository,geofenceRepository: GeofenceRepository,messageRepository: MessageRepository,accountRepository: AccountRepository) = SettingUsecases(
        getDarkTheme = GetDarkTheme(settingRepository),
        getDynamicTheme = GetDynamicTheme(settingRepository),
        getNoticeAlarm = GetNoticeAlarm(settingRepository),
        getScheduleAlarm = GetScheduleAlarm(settingRepository),
        getTriggerToggle = GetTriggerToggle(settingRepository),
        updateDarkTheme = UpdateDarkTheme(settingRepository),
        updateDynamicTheme = UpdateDynamicTheme(settingRepository),
        updateNoticeAlarm = UpdateNoticeAlarm(settingRepository),
        updateScheduleAlarm = UpdateScheduleAlarm(settingRepository),
        updateTriggerToggle = UpdateTriggerToggle(settingRepository),
        startGeofenceToClient = StartGeofenceToClient(geofenceRepository),
        removeGeofence = RemoveGeofence(geofenceRepository),
        subscribeNotice = SubscribeNotice(messageRepository),
        unsubscribeNotice = UnsubscribeNotice(messageRepository),
        signOut = SignOut(accountRepository)
    )

    @Provides
    @Singleton
    fun providesUserUseCases(userRepository: UserRepository) = UserUsecases(
        getUserInfo = GetUserInfo(userRepository),
        updateUserNickName = UpdateUserNickName(userRepository),
        updateUserGrade = UpdateUserGrade(userRepository),
        uploadImageUrl = UploadImageUrl(userRepository)
    )

    @Provides
    @Singleton
    fun providesGeoUseCases(recordRepository: RecordRepository,geofenceRepository: GeofenceRepository,settingRepository: SettingRepository) : GeoUsecases{
        return GeoUsecases(
            getCurrentRecord = GetCurrentRecord(recordRepository),
            updateRecord = UpdateRecord(recordRepository),
            insertRecord = InsertRecord(recordRepository),
            getChooseGeofence = GetChooseGeofence(geofenceRepository),
            getGeoState = GetGeoState(settingRepository),
            updateGeoState = UpdateGeoState(settingRepository),
            getTriggerToggle = GetTriggerToggle(settingRepository),
            startGeofenceToClient = StartGeofenceToClient(geofenceRepository),
            getMoveState = GetMoveState(settingRepository),
            updateMoveState = UpdateMoveState(settingRepository),
            removeGeofence = RemoveGeofence(geofenceRepository)
        )
    }

    @Provides
    @Singleton
    fun providesTriggerUseCases(geofenceRepository: GeofenceRepository) = TriggerUsecases(
        removeGeofence = RemoveGeofence(geofenceRepository),
        getGeoTriggerList = GetGeoTriggerList(geofenceRepository)
    )

    @Provides
    @Singleton
    fun providesTriggerSettingUseCases(geofenceRepository: GeofenceRepository,tagRepository: TagRepository) = TriggerSettingUsecases(
        addGeofence = AddGeofence(geofenceRepository),
        updateGeofence = UpdateGeofence(geofenceRepository),
        getAllTag = GetAllTag(tagRepository)
    )

    @Provides
    @Singleton
    fun providesAlarmUseCases(settingRepository: SettingRepository,scheduleRepository: ScheduleRepository) = AlarmUsecases(
        getNoticeAlarm = GetNoticeState(settingRepository),
        getScheduleAlarm = GetScheduleState(settingRepository),
        getAlarmSchedule = GetAlarmSchedule(scheduleRepository)
    )

    @Provides
    @Singleton
    fun providesRecordAlarmUseCases(recordRepository: RecordRepository,settingRepository: SettingRepository,geofenceRepository: GeofenceRepository,tagRepository: TagRepository) = RecordAlarmUsecases(
        getNowRecord = GetNowRecord(recordRepository),
        getCurrentRecord = GetCurrentRecord(recordRepository),
        insertRecord = InsertRecord(recordRepository),
        updateRecord = UpdateRecord(recordRepository),
        getRecordAlarm = GetRecordAlarm(settingRepository),
        addGeofence = AddGeofence(geofenceRepository),
        getNextTag = GetNextTag(tagRepository),
        removeGeofence = RemoveGeofence(geofenceRepository),
        getTempGeoTrigger = GetTempGeoTrigger(geofenceRepository)
    )


}