package com.beank.workFlowy.screen.setting

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.beank.domain.model.onEmpty
import com.beank.domain.model.onException
import com.beank.domain.model.onLoading
import com.beank.domain.model.onSuccess
import com.beank.domain.repository.LogRepository
import com.beank.domain.usecase.SettingUsecases
import com.beank.domain.usecase.UserUsecases
import com.beank.domain.usecase.account.SignOut
import com.beank.workFlowy.component.snackbar.SnackbarManager
import com.beank.workFlowy.screen.WorkFlowyViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import com.beank.presentation.R.string as AppText


@HiltViewModel
class SettingViewModel @Inject constructor(
    private val settingUsecases: SettingUsecases,
    private val userUsecases: UserUsecases,
    private val signOut: SignOut,
    logRepository: LogRepository
) : WorkFlowyViewModel(logRepository) {

    val uiState = SettingUiState()

    init {
        getUserInfo()
        getDarkThemeInfo()
        getDynamicThemeInfo()
        getNoticeAlarmInfo()
        getScheduleAlarmInfo()
        getTriggerInfo()
    }

    fun onImageUpload(uri: Uri, onFail : () -> Unit){
        launchCatching {
            uiState.userProgress = true
            userUsecases.uploadImageUrl(uri, onFail)
        }
    }

    fun onNoticeAlarmUpdate(toggle : Boolean){
        launchCatching {
            uiState.noticeToggle = toggle
            settingUsecases.updateNoticeAlarm(toggle)
        }
    }

    fun onScheduleAlarmUpdate(toggle : Boolean){
        launchCatching {
            uiState.scheduleToggle = toggle
            settingUsecases.updateScheduleAlarm(toggle)
        }
    }

    fun onDarkThemeUpdate(toggle : Boolean){
        launchCatching {
            uiState.darkThemeToggle = toggle
            settingUsecases.updateDarkTheme(toggle)
        }
    }

    fun onDynamicThemeUpdate(toggle : Boolean){
        launchCatching {
            uiState.dynamicThemeToggle = toggle
            settingUsecases.updateDynamicTheme(toggle)
        }
    }

    fun onTriggerToggleUpdate(toggle: Boolean){
        launchCatching {
            if (toggle) {
                settingUsecases.startGeofenceToClient()//등록된 트리거 백그라운드 올리기
            }else{
                settingUsecases.removeGeofence()//트리거 해제
            }
            uiState.triggerToggle = toggle
            settingUsecases.updateTriggerToggle(toggle)
        }
    }

    fun onNicknameUpdate() {
        launchCatching {
            userUsecases.updateUserNickName(uiState.tempNickname)
        }
    }

    fun onTempNicknameUpdate(name : String){
        uiState.tempNickname = name
    }

    fun onNicknameRefresh(){
        uiState.tempNickname = uiState.nickname
    }


    fun signOut() = signOut.invoke()


    private fun getDarkThemeInfo() = settingUsecases.getDarkTheme()
        .flowOn(Dispatchers.IO).onEach {toggle ->
            uiState.darkThemeToggle = toggle
        }.launchIn(viewModelScope)

    private fun getDynamicThemeInfo() = settingUsecases.getDynamicTheme()
        .flowOn(Dispatchers.IO).onEach {toggle ->
            uiState.dynamicThemeToggle = toggle
        }.launchIn(viewModelScope)

    private fun getNoticeAlarmInfo() = settingUsecases.getNoticeAlarm()
        .flowOn(Dispatchers.IO).onEach {toggle ->
            uiState.noticeToggle = toggle
        }.launchIn(viewModelScope)

    private fun getScheduleAlarmInfo() = settingUsecases.getScheduleAlarm()
        .flowOn(Dispatchers.IO).onEach {toggle ->
            uiState.scheduleToggle = toggle
        }.launchIn(viewModelScope)

    private fun getTriggerInfo() = settingUsecases.getTriggerToggle()
        .flowOn(Dispatchers.IO).onEach { toggle ->
            uiState.triggerToggle = toggle
        }.launchIn(viewModelScope)

    private fun getUserInfo() = userUsecases.getUserInfo()
        .flowOn(Dispatchers.IO).onEach { state ->
            state.onSuccess {user ->
                uiState.nickname = user[0].nickname
                uiState.grade = user[0].grade
                uiState.urlImage = user[0].urlImage
                uiState.userProgress = false
                uiState.tempNickname = user[0].nickname
            }
            state.onEmpty {
                uiState.userProgress = false
            }
            state.onLoading {
                uiState.userProgress = true
            }
            state.onException { message, e ->
                uiState.userProgress = false
                e.message?.let {
                    if (!it.contains("PERMISSION_DENIED")){
                        SnackbarManager.showMessage(AppText.firebase_server_error)
                        logFirebaseFatalCrash(message, e)
                    }
                }
            }
        }.launchIn(viewModelScope)
}