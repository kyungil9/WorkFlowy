package com.beank.workFlowy.screen.sign_up

import android.os.Build
import androidx.annotation.RequiresApi
import com.beank.domain.repository.LogRepository
import com.beank.domain.usecase.SignUpUsecases
import com.beank.presentation.R
import com.beank.workFlowy.component.snackbar.SnackbarManager
import com.beank.workFlowy.screen.WorkFlowyViewModel
import com.beank.workFlowy.utils.isValidEmail
import com.beank.workFlowy.utils.isValidPassword
import com.beank.workFlowy.utils.passwordMatches
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUsecases: SignUpUsecases,
    logRepository: LogRepository
) : WorkFlowyViewModel(logRepository){

    val uiState = SignUpUiState()

    fun onEmailChange(newValue : String){
        uiState.email = newValue
    }

    fun onPasswordChange(newValue: String){
        uiState.password = newValue
    }

    fun onRepeatPasswordChange(newValue: String){
        uiState.repeatPassword = newValue
    }

    fun onSignInClick(onBack: () -> Unit) {
        if (!uiState.email.isValidEmail()) {
            SnackbarManager.showMessage(R.string.email_error)
            return
        }

        if (!uiState.password.isValidPassword()) {
            SnackbarManager.showMessage(R.string.password_error)
            return
        }

        if (!uiState.password.passwordMatches(uiState.repeatPassword)) {
            SnackbarManager.showMessage(R.string.password_match_error)
            return
        }

        launchCatching {
            signUpUsecases.createAccount(
                email = uiState.email,
                password = uiState.password,
                onSuccess = {
                    signUpUsecases.initDataSetting()
                    onBack()},
                onFailMessage = {SnackbarManager.showMessage(R.string.login_server_error)}
            )
        }
    }

}