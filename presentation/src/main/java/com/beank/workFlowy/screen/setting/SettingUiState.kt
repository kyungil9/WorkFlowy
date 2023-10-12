package com.beank.workFlowy.screen.setting

import android.net.Uri
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Stable
class SettingUiState(
    nickname : String = "",
    grade : Int = 0,
    urlImage : Uri? = null,
    userProgress : Boolean = true,
    darkThemeToggle : Boolean = false,
    dynamicThemeToggle : Boolean = false,
    noticeToggle : Boolean = true,
    scheduleToggle : Boolean = true,
    triggerToggle : Boolean = false,
    tempNickname : String = ""
){
    var nickname by mutableStateOf(nickname)
    var grade by mutableIntStateOf(grade)
    var urlImage by mutableStateOf(urlImage)
    var userProgress by mutableStateOf(userProgress)
    var darkThemeToggle by mutableStateOf(darkThemeToggle)
    var dynamicThemeToggle by mutableStateOf(dynamicThemeToggle)
    var noticeToggle by mutableStateOf(noticeToggle)
    var scheduleToggle by mutableStateOf(scheduleToggle)
    var triggerToggle by mutableStateOf(triggerToggle)
    var tempNickname by mutableStateOf(tempNickname)
}
