package com.beank.domain.usecase.setting

import com.beank.domain.repository.SettingRepository
import javax.inject.Inject

class InitSetting @Inject constructor(
    private val settingRepository: SettingRepository
) {
    suspend operator fun invoke(value : Boolean) = settingRepository.initSetting(value)
}