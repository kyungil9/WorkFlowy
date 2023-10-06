package com.beank.workFlowy.screen.setting

import android.net.Uri
import android.util.Log
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import com.beank.presentation.R.string as AppText


@HiltViewModel
class SettingViewModel @Inject constructor(
    private val settingUsecases: SettingUsecases,
    private val userUsecases: UserUsecases,
    private val signOut: SignOut,
    logRepository: LogRepository
) : WorkFlowyViewModel(logRepository) {

    private val _uiState = MutableStateFlow(SettingUiState())
    private val uiState get() = _uiState.asStateFlow()

    val nickname get() = uiState.map { it.userInfo.nickname }
    val tempNickname get() = uiState.map { it.tempNickname }
    val grade get() = uiState.map { it.userInfo.grade }
    val userProgress get() = uiState.map { it.userProgress }
    val urlImage get() = uiState.map { it.userInfo.urlImage }
    val darkTheme get() = uiState.map { it.darkThemeToggle }
    val dynamicTheme get() = uiState.map { it.dynamicThemeToggle }
    val noticeAlarm get() = uiState.map { it.noticeToggle }
    val scheduleAlarm get() = uiState.map { it.scheduleToggle }


    init {
        getUserInfo()
        getDarkThemeInfo()
        getDynamicThemeInfo()
        getNoticeAlarmInfo()
        getScheduleAlarmInfo()
    }

    fun onImageUpload(uri: Uri, onFail : () -> Unit){
        launchCatching {
            _uiState.update { it.copy(userProgress = true) }
            userUsecases.uploadImageUrl(uri, onFail)
        }
    }

    fun onNoticeAlarmUpdate(toggle : Boolean){
        launchCatching {
            _uiState.update { it.copy(noticeToggle = toggle) }
            settingUsecases.updateNoticeAlarm(toggle)
        }
    }

    fun onScheduleAlarmUpdate(toggle : Boolean){
        launchCatching {
            _uiState.update { it.copy(scheduleToggle = toggle) }
            settingUsecases.updateScheduleAlarm(toggle)
        }
    }

    fun onDarkThemeUpdate(toggle : Boolean){
        launchCatching {
            _uiState.update { it.copy(darkThemeToggle = toggle) }
            settingUsecases.updateDarkTheme(toggle)
        }
    }

    fun onDynamicThemeUpdate(toggle : Boolean){
        launchCatching {
            _uiState.update { it.copy(dynamicThemeToggle = toggle) }
            settingUsecases.updateDynamicTheme(toggle)
        }
    }

    fun onNicknameUpdate() {
        launchCatching {
            userUsecases.updateUserNickName(_uiState.value.tempNickname)
        }
    }

    fun onTempNicknameUpdate(name : String){
        _uiState.update { it.copy(tempNickname = name) }
    }

    fun onNicknameRefresh(){
        _uiState.update { it.copy(tempNickname = uiState.value.userInfo.nickname) }
    }


    fun signOut() = signOut.invoke()


    private fun getDarkThemeInfo() = settingUsecases.getDarkTheme()
        .flowOn(Dispatchers.IO).onEach {toggle ->
            _uiState.update { it.copy(darkThemeToggle = toggle) }
        }.launchIn(viewModelScope)

    private fun getDynamicThemeInfo() = settingUsecases.getDynamicTheme()
        .flowOn(Dispatchers.IO).onEach {toggle ->
            _uiState.update { it.copy(dynamicThemeToggle = toggle) }
        }.launchIn(viewModelScope)

    private fun getNoticeAlarmInfo() = settingUsecases.getNoticeAlarm()
        .flowOn(Dispatchers.IO).onEach {toggle ->
            _uiState.update { it.copy(noticeToggle = toggle) }
        }.launchIn(viewModelScope)

    private fun getScheduleAlarmInfo() = settingUsecases.getScheduleAlarm()
        .flowOn(Dispatchers.IO).onEach {toggle ->
            _uiState.update { it.copy(scheduleToggle = toggle) }
        }.launchIn(viewModelScope)

    private fun getUserInfo() = userUsecases.getUserInfo()
        .flowOn(Dispatchers.IO).onEach { state ->
            state.onSuccess {user ->
                _uiState.update { it.copy(userInfo = user[0], userProgress = false, tempNickname = user[0].nickname) }
            }
            state.onEmpty {
                _uiState.update { it.copy(userProgress = false) }
            }
            state.onLoading {
                _uiState.update { it.copy(userProgress = true) }
            }
            state.onException { message, e ->
                _uiState.update { it.copy(userProgress = false) }
                e.message?.let {
                    if (!it.contains("PERMISSION_DENIED")){
                        SnackbarManager.showMessage(AppText.firebase_server_error)
                        logFirebaseFatalCrash(message, e)
                    }
                }
            }
        }.launchIn(viewModelScope)
}