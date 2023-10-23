package com.beank.domain.usecase

import com.beank.domain.usecase.schedule.GetAlarmSchedule
import com.beank.domain.usecase.setting.GetNoticeAlarm
import com.beank.domain.usecase.setting.GetNoticeState
import com.beank.domain.usecase.setting.GetScheduleAlarm
import com.beank.domain.usecase.setting.GetScheduleState

data class AlarmUsecases(
    val getNoticeAlarm: GetNoticeState,
    val getScheduleAlarm: GetScheduleState,
    val getAlarmSchedule: GetAlarmSchedule
)