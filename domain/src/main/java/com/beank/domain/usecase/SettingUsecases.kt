package com.beank.domain.usecase

import com.beank.domain.usecase.account.SignOut
import com.beank.domain.usecase.geo.RemoveGeofence
import com.beank.domain.usecase.geo.RemoveMoveToClient
import com.beank.domain.usecase.geo.StartGeofenceToClient
import com.beank.domain.usecase.geo.StartMoveToClient
import com.beank.domain.usecase.message.SubscribeNotice
import com.beank.domain.usecase.message.UnsubscribeNotice
import com.beank.domain.usecase.setting.GetDarkTheme
import com.beank.domain.usecase.setting.GetDynamicTheme
import com.beank.domain.usecase.setting.GetMoveTriggerToggle
import com.beank.domain.usecase.setting.GetNoticeAlarm
import com.beank.domain.usecase.setting.GetRecordAlarm
import com.beank.domain.usecase.setting.GetScheduleAlarm
import com.beank.domain.usecase.setting.GetTriggerToggle
import com.beank.domain.usecase.setting.UpdateDarkTheme
import com.beank.domain.usecase.setting.UpdateDynamicTheme
import com.beank.domain.usecase.setting.UpdateMoveTriggerToggle
import com.beank.domain.usecase.setting.UpdateNoticeAlarm
import com.beank.domain.usecase.setting.UpdateRecordAlarm
import com.beank.domain.usecase.setting.UpdateScheduleAlarm
import com.beank.domain.usecase.setting.UpdateTriggerToggle

data class SettingUsecases(
    val getDarkTheme: GetDarkTheme,
    val getDynamicTheme: GetDynamicTheme,
    val getNoticeAlarm: GetNoticeAlarm,
    val getScheduleAlarm: GetScheduleAlarm,
    val getTriggerToggle: GetTriggerToggle,
    val getMoveTriggerToggle: GetMoveTriggerToggle,
    val getRecordAlarm: GetRecordAlarm,
    val updateDarkTheme: UpdateDarkTheme,
    val updateDynamicTheme: UpdateDynamicTheme,
    val updateNoticeAlarm: UpdateNoticeAlarm,
    val updateScheduleAlarm: UpdateScheduleAlarm,
    val updateTriggerToggle: UpdateTriggerToggle,
    val updateMoveTriggerToggle: UpdateMoveTriggerToggle,
    val updateRecordAlarm: UpdateRecordAlarm,
    val startGeofenceToClient: StartGeofenceToClient,
    val startMoveToClient: StartMoveToClient,
    val removeMoveToClient: RemoveMoveToClient,
    val removeGeofence: RemoveGeofence,
    val subscribeNotice: SubscribeNotice,
    val unsubscribeNotice: UnsubscribeNotice,
    val signOut: SignOut
)