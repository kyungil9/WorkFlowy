package com.beank.domain.usecase.setting

import com.beank.domain.repository.SettingRepository
import javax.inject.Inject

class UpdateMoveTriggerToggle @Inject constructor(
    private val settingRepository: SettingRepository
) {
    suspend operator fun invoke(state : Boolean) = settingRepository.updateMoveTriggerToggle(state)
}