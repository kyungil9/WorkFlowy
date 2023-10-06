package com.beank.workFlowy.screen.login

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.beank.domain.repository.LogRepository
import com.beank.domain.usecase.LoginUsecases
import com.beank.workFlowy.component.snackbar.SnackbarManager
import com.beank.workFlowy.navigation.NavigationItem
import com.beank.workFlowy.screen.WorkFlowyViewModel
import com.beank.workFlowy.utils.isValidEmail
import com.beank.workFlowy.utils.isValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import com.beank.presentation.R.string as AppText


@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUsecases: LoginUsecases,
    logRepository: LogRepository
) : WorkFlowyViewModel(logRepository){

    private val _uiState = MutableStateFlow(LoginUiState())
    private val uiState get() = _uiState.asStateFlow()

    val email get() = uiState.map { it.email }
    val password get() = uiState.map { it.password }


    fun onEmailChange(newValue : String){
        _uiState.update { it.copy(email = newValue) }
    }

    fun onPasswordChange(newValue: String){
        _uiState.update { it.copy(password = newValue) }
    }

    fun initSetting(){
        launchCatching {
            loginUsecases.initDataSetting()
        }
    }

    fun initToken() = loginUsecases.insertToken()

    fun onSignInClick(openAndPopUp: (String, String) -> Unit) {
        if (!uiState.value.email.isValidEmail()) {
            SnackbarManager.showMessage(AppText.email_error)
            return
        }

        if (uiState.value.password.isBlank()) {
            SnackbarManager.showMessage(AppText.empty_password_error)
            return
        }

        if (!uiState.value.password.isValidPassword()) {
            SnackbarManager.showMessage(AppText.password_error)
            return
        }

        launchCatching {
            loginUsecases.loginAccount(
                email = uiState.value.email,
                password = uiState.value.password,
                onSuccess = {
                    initToken()
                    openAndPopUp(NavigationItem.HOME.route, NavigationItem.LOGIN.route) },
                onFailMessage = {SnackbarManager.showMessage(AppText.login_server_error)}
            )
        }
    }

}