package com.beank.workFlowy.screen.setting

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.viewModelScope
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.beank.domain.model.onEmpty
import com.beank.domain.model.onException
import com.beank.domain.model.onLoading
import com.beank.domain.model.onSuccess
import com.beank.domain.repository.LogRepository
import com.beank.domain.usecase.SettingUsecases
import com.beank.domain.usecase.UserUsecases
import com.beank.workFlowy.component.snackbar.SnackbarManager
import com.beank.workFlowy.screen.WorkFlowyViewModel
import com.beank.workFlowy.utils.MessageMode
import com.beank.workFlowy.utils.MessageWorkRequest
import com.beank.workFlowy.utils.RecordMode
import com.beank.workFlowy.utils.RecordWorkRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import com.beank.presentation.R.string as AppText


@HiltViewModel
class SettingViewModel @Inject constructor(
    private val settingUsecases: SettingUsecases,
    private val userUsecases: UserUsecases,
    private val workManager: WorkManager,
    @MessageWorkRequest private val messageRequest : OneTimeWorkRequest.Builder,
    @RecordWorkRequest private val recordRequest : OneTimeWorkRequest.Builder,
    val recordIntent : Intent,
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
        getTriggerMoveInfo()
        getRecordAlarmInfo()
    }

    fun onImageUpload(uri: Uri, onFail : () -> Unit){
        launchCatching {
            uiState.userProgress = true
            userUsecases.uploadImageUrl(uri, onFail)
        }
    }

    fun onNoticeAlarmUpdate(toggle : Boolean){
        launchCatching {
            if (toggle){
                settingUsecases.subscribeNotice()
            }else{
                settingUsecases.unsubscribeNotice()
            }
            settingUsecases.updateNoticeAlarm(toggle)
        }
    }

    fun onScheduleAlarmUpdate(toggle : Boolean){
        launchCatching {
            if (toggle){
                val messageWorkRequest = messageRequest
                    .setInputData(workDataOf("mode" to MessageMode.REPEAT))
                    .setBackoffCriteria(BackoffPolicy.LINEAR,30000, TimeUnit.MILLISECONDS)
                    .build()
                workManager.enqueue(messageWorkRequest)
            }else{
                val messageWorkRequest = messageRequest
                    .setInputData(workDataOf("mode" to MessageMode.CANCLEALL))
                    .setBackoffCriteria(BackoffPolicy.LINEAR,30000, TimeUnit.MILLISECONDS)
                    .build()
                workManager.enqueue(messageWorkRequest)
            }
            settingUsecases.updateScheduleAlarm(toggle)
        }
    }

    fun onDarkThemeUpdate(toggle : Boolean){
        launchCatching {
            settingUsecases.updateDarkTheme(toggle)
        }
    }

    fun onDynamicThemeUpdate(toggle : Boolean){
        launchCatching {
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
            settingUsecases.updateTriggerToggle(toggle)
        }
    }

    fun onTriggerMoveUpdate(toggle: Boolean){
        launchCatching {
            if (toggle){
                settingUsecases.startMoveToClient()
            }else{
                settingUsecases.removeMoveToClient()
            }
            settingUsecases.updateMoveTriggerToggle(toggle)
        }
    }

    fun onRecordAlarmUpdate(toggle: Boolean){
        launchCatching {
            settingUsecases.updateRecordAlarm(toggle)
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


    fun signOut() {
        launchCatching {
            settingUsecases.signOut()
            settingUsecases.unsubscribeNotice()
            if (uiState.scheduleToggle){
                val messageWorkRequest = messageRequest
                    .setInputData(workDataOf("mode" to MessageMode.CANCLEALL))
                    .setBackoffCriteria(BackoffPolicy.LINEAR,30000, TimeUnit.MILLISECONDS)
                    .build()
                workManager.enqueue(messageWorkRequest)
            }
            if (uiState.recordAlarmToggle){
                val recordMessageWorkRequest = recordRequest
                    .setInputData(workDataOf("mode" to RecordMode.STOP))
                    .setBackoffCriteria(BackoffPolicy.LINEAR,30000, TimeUnit.MILLISECONDS)
                    .build()
                workManager.enqueue(recordMessageWorkRequest)
            }
            onTriggerToggleUpdate(false)
            onTriggerMoveUpdate(false)
        }
    }


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

    private fun getTriggerMoveInfo() = settingUsecases.getMoveTriggerToggle()
        .flowOn(Dispatchers.IO).onEach { toggle ->
            uiState.triggerMoveToggle = toggle
        }.launchIn(viewModelScope)

    private fun getRecordAlarmInfo() = settingUsecases.getRecordAlarm()
        .flowOn(Dispatchers.IO).onEach { toggle ->
            uiState.recordAlarmToggle = toggle
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