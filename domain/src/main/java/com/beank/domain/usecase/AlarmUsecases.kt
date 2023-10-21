package com.beank.domain.usecase

import com.beank.domain.usecase.schedule.GetAlarmSchedule
import com.beank.domain.usecase.setting.GetNoticeAlarm
import com.beank.domain.usecase.setting.GetScheduleAlarm

data class AlarmUsecases(
    val getNoticeAlarm: GetNoticeAlarm,
    val getScheduleAlarm: GetScheduleAlarm,
    val getAlarmSchedule: GetAlarmSchedule
)