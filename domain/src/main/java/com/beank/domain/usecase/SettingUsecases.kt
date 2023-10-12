package com.beank.domain.usecase

import com.beank.domain.usecase.geo.RemoveGeofence
import com.beank.domain.usecase.geo.StartGeofenceToClient
import com.beank.domain.usecase.setting.GetDarkTheme
import com.beank.domain.usecase.setting.GetDynamicTheme
import com.beank.domain.usecase.setting.GetNoticeAlarm
import com.beank.domain.usecase.setting.GetScheduleAlarm
import com.beank.domain.usecase.setting.GetTriggerToggle
import com.beank.domain.usecase.setting.UpdateDarkTheme
import com.beank.domain.usecase.setting.UpdateDynamicTheme
import com.beank.domain.usecase.setting.UpdateNoticeAlarm
import com.beank.domain.usecase.setting.UpdateScheduleAlarm
import com.beank.domain.usecase.setting.UpdateTriggerToggle

data class SettingUsecases(
    val getDarkTheme: GetDarkTheme,
    val getDynamicTheme: GetDynamicTheme,
    val getNoticeAlarm: GetNoticeAlarm,
    val getScheduleAlarm: GetScheduleAlarm,
    val getTriggerToggle: GetTriggerToggle,
    val updateDarkTheme: UpdateDarkTheme,
    val updateDynamicTheme: UpdateDynamicTheme,
    val updateNoticeAlarm: UpdateNoticeAlarm,
    val updateScheduleAlarm: UpdateScheduleAlarm,
    val updateTriggerToggle: UpdateTriggerToggle,
    val startGeofenceToClient: StartGeofenceToClient,
    val removeGeofence: RemoveGeofence
)