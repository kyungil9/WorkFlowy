package com.beank.workFlowy.screen.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beank.domain.service.AccountService
import com.beank.domain.service.LogService
import com.beank.workFlowy.R
import com.beank.workFlowy.component.snackbar.SnackbarManager
import com.beank.workFlowy.navigation.NavigationItem
import com.beank.workFlowy.screen.WorkFlowyViewModel
import com.beank.workFlowy.utils.isValidEmail
import com.beank.workFlowy.utils.isValidPassword
import com.beank.workFlowy.utils.passwordMatches
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val accountService: AccountService,
    logService: LogService
) : WorkFlowyViewModel(logService){

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
            accountService.createAccount(
                email = inputEmail.value,
                password = inputPassword.value,
                onSuccess = {onBack()},
                onFailMessage = {SnackbarManager.showMessage(R.string.login_server_error)}
            )
        }
    }

}