package com.beank.workFlowy.screen.login

import android.os.Build
import androidx.annotation.RequiresApi
import com.beank.domain.repository.LogRepository
import com.beank.domain.usecase.LoginUsecases
import com.beank.workFlowy.component.snackbar.SnackbarManager
import com.beank.workFlowy.navigation.NavigationItem
import com.beank.workFlowy.screen.WorkFlowyViewModel
import com.beank.workFlowy.utils.isValidEmail
import com.beank.workFlowy.utils.isValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.beank.presentation.R.string as AppText


@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUsecases: LoginUsecases,
    logRepository: LogRepository
) : WorkFlowyViewModel(logRepository){

    val uiState = LoginUiState()

    fun onEmailChange(newValue : String){
        uiState.email = newValue
    }

    fun onPasswordChange(newValue: String){
        uiState.password = newValue
    }

    fun initSetting(){
        launchCatching {
            loginUsecases.initDataSetting()
        }
    }

    fun initToken() = loginUsecases.insertToken()

    fun onSignInClick(openAndPopUp: (String, String) -> Unit) {
        if (!uiState.email.isValidEmail()) {
            SnackbarManager.showMessage(AppText.email_error)
            return
        }

        if (uiState.password.isBlank()) {
            SnackbarManager.showMessage(AppText.empty_password_error)
            return
        }

        if (!uiState.password.isValidPassword()) {
            SnackbarManager.showMessage(AppText.password_error)
            return
        }

        launchCatching {
            loginUsecases.loginAccount(
                email = uiState.email,
                password = uiState.password,
                onSuccess = {
                    initToken()
                    openAndPopUp(NavigationItem.HOME.route, NavigationItem.LOGIN.route) },
                onFailMessage = {SnackbarManager.showMessage(AppText.login_server_error)}
            )
        }
    }

}