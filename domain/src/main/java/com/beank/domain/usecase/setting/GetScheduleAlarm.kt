package com.beank.domain.usecase.setting

import com.beank.domain.repository.SettingRepository
import javax.inject.Inject

class GetScheduleAlarm @Inject constructor(
    private val settingRepository: SettingRepository
) {
    operator fun invoke() = settingRepository.getScheduleAlarm()
}