package com.beank.workFlowy.screen.sign_up

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    var uiState by mutableStateOf(SignUpUiState())
        private set
    private val email get() = uiState.email
    private val password get() = uiState.password
    private val repeatPassword get() =  uiState.repeatPassword

    fun onEmailChange(newValue : String){
        uiState = uiState.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String){
        uiState = uiState.copy(password = newValue)
    }

    fun onRepeatPasswordChange(newValue: String){
        uiState = uiState.copy(repeatPassword = newValue)
    }

    fun onSignInClick(onBack: () -> Unit) {
        if (!email.isValidEmail()) {
            SnackbarManager.showMessage(R.string.email_error)
            return
        }

        if (!password.isValidPassword()) {
            SnackbarManager.showMessage(R.string.password_error)
            return
        }

        if (!password.passwordMatches(repeatPassword)) {
            SnackbarManager.showMessage(R.string.password_match_error)
            return
        }

        launchCatching {
            signUpUsecases.createAccount(
                email = email,
                password = password,
                onSuccess = {
                    signUpUsecases.initDataSetting()
                    onBack()},
                onFailMessage = {SnackbarManager.showMessage(R.string.login_server_error)}
            )
        }
    }

}