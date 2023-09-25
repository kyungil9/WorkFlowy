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
import javax.inject.Inject
import com.beank.presentation.R.string as AppText


@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUsecases: LoginUsecases,
    logRepository: LogRepository
) : WorkFlowyViewModel(logRepository){

    var inputEmail by mutableStateOf("")
        private set
    var inputPassword by mutableStateOf("")
        private set

    fun onEmailChange(newValue : String){
        inputEmail = newValue
    }

    fun onPasswordChange(newValue: String){
        inputPassword = newValue
    }

    fun initSetting(){
        launchCatching {
            loginUsecases.initDataSetting()
        }
    }

    fun onSignInClick(openAndPopUp: (String, String) -> Unit) {
        if (!inputEmail.isValidEmail()) {
            SnackbarManager.showMessage(AppText.email_error)
            return
        }

        if (inputPassword.isBlank()) {
            SnackbarManager.showMessage(AppText.empty_password_error)
            return
        }

        if (!inputPassword.isValidPassword()) {
            SnackbarManager.showMessage(AppText.password_error)
            return
        }

        launchCatching {
            loginUsecases.loginAccount(
                email = inputEmail,
                password = inputPassword,
                onSuccess = { openAndPopUp(NavigationItem.HOME.route, NavigationItem.LOGIN.route) },
                onFailMessage = {SnackbarManager.showMessage(AppText.login_server_error)}
            )
        }
    }

}