package com.beank.workFlowy.screen.sign_up

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.beank.domain.repository.AccountRepository
import com.beank.domain.repository.LogRepository
import com.beank.domain.usecase.SignUpUsecases
import com.beank.domain.usecase.account.CreateAccount
import com.beank.domain.usecase.tag.InitDataSetting
import com.beank.presentation.R
import com.beank.workFlowy.component.snackbar.SnackbarManager
import com.beank.workFlowy.screen.WorkFlowyViewModel
import com.beank.workFlowy.utils.isValidEmail
import com.beank.workFlowy.utils.isValidPassword
import com.beank.workFlowy.utils.passwordMatches
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    //private val signUpUsecases: SignUpUsecases,
    logRepository: LogRepository
) : WorkFlowyViewModel(logRepository){

    var inputEmail by mutableStateOf("")
        private set
    var inputPassword by mutableStateOf("")
        private set
    var inputRepeatPassword by mutableStateOf("")
        private set

    fun onEmailChange(newValue : String){
        inputEmail = newValue
    }

    fun onPasswordChange(newValue: String){
        inputPassword = newValue
    }

    fun onRepeatPassword(newValue: String){
        inputRepeatPassword = newValue
    }

    fun onSignInClick(onBack: () -> Unit) {
        if (!inputEmail.isValidEmail()) {
            SnackbarManager.showMessage(R.string.email_error)
            return
        }

        if (inputPassword.isValidPassword()) {
            SnackbarManager.showMessage(R.string.password_error)
            return
        }

        if (!inputPassword.passwordMatches(inputRepeatPassword)) {
            SnackbarManager.showMessage(R.string.password_match_error)
            return
        }

        launchCatching {
//            signUpUsecases.createAccount(
//                email = inputEmail,
//                password = inputPassword,
//                onSuccess = {
//                    signUpUsecases.initDataSetting()
//                    onBack()},
//                onFailMessage = {SnackbarManager.showMessage(R.string.login_server_error)}
//            )
        }
    }

}