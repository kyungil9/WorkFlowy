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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUsecases: SignUpUsecases,
    logRepository: LogRepository
) : WorkFlowyViewModel(logRepository){

    private val _uiState = MutableStateFlow(SignUpUiState())
    private val uiState get() = _uiState.asStateFlow()

    val email get() = uiState.map { it.email }
    val password get() = uiState.map { it.password }
    val repeatPassword get() =  uiState.map { it.repeatPassword }

    fun onEmailChange(newValue : String){
        _uiState.update { it.copy(email = newValue) }
    }

    fun onPasswordChange(newValue: String){
        _uiState.update { it.copy(password = newValue) }
    }

    fun onRepeatPasswordChange(newValue: String){
        _uiState.update { it.copy(repeatPassword = newValue) }
    }

    fun onSignInClick(onBack: () -> Unit) {
        if (!uiState.value.email.isValidEmail()) {
            SnackbarManager.showMessage(R.string.email_error)
            return
        }

        if (!uiState.value.password.isValidPassword()) {
            SnackbarManager.showMessage(R.string.password_error)
            return
        }

        if (!uiState.value.password.passwordMatches(uiState.value.repeatPassword)) {
            SnackbarManager.showMessage(R.string.password_match_error)
            return
        }

        launchCatching {
            signUpUsecases.createAccount(
                email = uiState.value.email,
                password = uiState.value.password,
                onSuccess = {
                    signUpUsecases.initDataSetting()
                    onBack()},
                onFailMessage = {SnackbarManager.showMessage(R.string.login_server_error)}
            )
        }
    }

}