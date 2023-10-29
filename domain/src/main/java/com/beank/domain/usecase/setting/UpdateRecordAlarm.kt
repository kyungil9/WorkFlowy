package com.beank.domain.usecase.setting

import com.beank.domain.repository.SettingRepository
import javax.inject.Inject

class UpdateRecordAlarm @Inject constructor(
    private val settingRepository: SettingRepository
){
    suspend operator fun invoke(state : Boolean) = settingRepository.updateRecordAlarm(state)
}