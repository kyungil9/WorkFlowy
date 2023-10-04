package com.beank.workFlowy.screen.setting

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    var uiState by mutableStateOf(SettingUiState())
        private set

    init {
        getUserInfo()
        getDarkThemeInfo()
        getDynamicThemeInfo()
        getNoticeAlarmInfo()
        getScheduleAlarmInfo()
    }

    fun onImageUpload(uri: Uri, onFail : () -> Unit){
        launchCatching {
            uiState = uiState.copy(userProgress = true)
            userUsecases.uploadImageUrl(uri, onFail)
        }
    }

    fun onNoticeAlarmUpdate(toggle : Boolean){
        launchCatching {
            uiState = uiState.copy(noticeToggle = toggle)
            settingUsecases.updateNoticeAlarm(toggle)
        }
    }

    fun onScheduleAlarmUpdate(toggle : Boolean){
        launchCatching {
            uiState = uiState.copy(scheduleToggle = toggle)
            settingUsecases.updateScheduleAlarm(toggle)
        }
    }

    fun onDarkThemeUpdate(toggle : Boolean){
        launchCatching {
            uiState = uiState.copy(darkThemeToggle = toggle)
            settingUsecases.updateDarkTheme(toggle)
        }
    }

    fun onDynamicThemeUpdate(toggle : Boolean){
        launchCatching {
            uiState = uiState.copy(dynamicThemeToggle = toggle)
            settingUsecases.updateDynamicTheme(toggle)
        }
    }

    fun onNicknameUpdate() {
        launchCatching {
            userUsecases.updateUserNickName(uiState.tempNickname)
        }
    }

    fun onTempNicknameUpdate(name : String){
        uiState = uiState.copy(tempNickname = name)
    }

    fun onNicknameRefresh(){
        uiState = uiState.copy(tempNickname = uiState.userInfo.nickname)
    }


    fun signOut() = signOut.invoke()


    private fun getDarkThemeInfo() = settingUsecases.getDarkTheme()
        .flowOn(Dispatchers.IO).onEach {
            uiState = uiState.copy(darkThemeToggle = it)
        }.launchIn(viewModelScope)

    private fun getDynamicThemeInfo() = settingUsecases.getDynamicTheme()
        .flowOn(Dispatchers.IO).onEach {
            uiState = uiState.copy(dynamicThemeToggle = it)
        }.launchIn(viewModelScope)

    private fun getNoticeAlarmInfo() = settingUsecases.getNoticeAlarm()
        .flowOn(Dispatchers.IO).onEach {
            uiState = uiState.copy(noticeToggle = it)
        }.launchIn(viewModelScope)

    private fun getScheduleAlarmInfo() = settingUsecases.getScheduleAlarm()
        .flowOn(Dispatchers.IO).onEach {
            uiState = uiState.copy(scheduleToggle = it)
        }.launchIn(viewModelScope)

    private fun getUserInfo() = userUsecases.getUserInfo()
        .flowOn(Dispatchers.IO).onEach { state ->
            state.onSuccess {
                uiState = uiState.copy(userInfo = it[0], userProgress = false, tempNickname = it[0].nickname)
            }
            state.onEmpty {
                uiState = uiState.copy(userProgress = false)
            }
            state.onLoading {
                uiState = uiState.copy(userProgress = true)
            }
            state.onException { message, e ->
                uiState = uiState.copy(userProgress = false)
                e.message?.let {
                    if (!it.contains("PERMISSION_DENIED")){
                        SnackbarManager.showMessage(AppText.firebase_server_error)
                        logFirebaseFatalCrash(message, e)
                    }
                }
            }
        }.launchIn(viewModelScope)
}