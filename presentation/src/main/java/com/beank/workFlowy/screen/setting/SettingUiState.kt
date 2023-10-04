package com.beank.workFlowy.screen.setting

import androidx.compose.runtime.Stable
import com.beank.domain.model.UserInfo
import com.beank.domain.usecase.setting.UpdateDynamicTheme

@Stable
data class SettingUiState(
    val userInfo : UserInfo = UserInfo(),
    val userProgress : Boolean = true,
    val darkThemeToggle : Boolean = false,
    val dynamicThemeToggle : Boolean = false,
    val noticeToggle : Boolean = true,
    val scheduleToggle : Boolean = true,
    val tempNickname : String = ""
)
