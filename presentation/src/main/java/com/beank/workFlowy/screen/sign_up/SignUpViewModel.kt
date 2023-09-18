package com.beank.workFlowy.screen.sign_up

import com.beank.domain.repository.AccountRepository
import com.beank.domain.repository.LogRepository
import com.beank.domain.usecase.account.CreateAccount
import com.beank.domain.usecase.tag.InitDataSetting
import com.beank.workFlowy.R
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
    private val createAccount: CreateAccount,
    private val initDataSetting: InitDataSetting,
    logRepository: LogRepository
) : WorkFlowyViewModel(logRepository){

    private val _inputEmail = MutableStateFlow("")
    private val _inputPassword = MutableStateFlow("")
    private val _inputRepeatPassword = MutableStateFlow("")

    val inputEmail get() = _inputEmail.asStateFlow()
    val inputPassword get() = _inputPassword.asStateFlow()
    val inputRepeatPassword get() = _inputRepeatPassword.asStateFlow()

    fun onEmailChange(newValue : String){
        _inputEmail.value = newValue
    }

    fun onPasswordChange(newValue: String){
        _inputPassword.value = newValue
    }

    fun onRepeatPassword(newValue: String){
        _inputRepeatPassword.value = newValue
    }

    fun onSignInClick(onBack: () -> Unit) {
        if (!inputEmail.value.isValidEmail()) {
            SnackbarManager.showMessage(R.string.email_error)
            return
        }

        if (inputPassword.value.isValidPassword()) {
            SnackbarManager.showMessage(R.string.password_error)
            return
        }

        if (!inputPassword.value.passwordMatches(inputRepeatPassword.value)) {
            SnackbarManager.showMessage(R.string.password_match_error)
            return
        }

        launchCatching {
            createAccount(
                email = inputEmail.value,
                password = inputPassword.value,
                onSuccess = {
                    initDataSetting()
                    onBack()},
                onFailMessage = {SnackbarManager.showMessage(R.string.login_server_error)}
            )
        }
    }

}