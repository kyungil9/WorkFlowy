package com.beank.workFlowy.screen.login

import com.beank.domain.repository.AccountRepository
import com.beank.domain.repository.LogRepository
import com.beank.data.datasource.StorageDataSource
import com.beank.domain.usecase.account.LoginAccount
import com.beank.domain.usecase.tag.InitDataSetting
import com.beank.workFlowy.component.snackbar.SnackbarManager
import com.beank.workFlowy.navigation.NavigationItem
import com.beank.workFlowy.screen.WorkFlowyViewModel
import com.beank.workFlowy.utils.isValidEmail
import com.beank.workFlowy.utils.isValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import com.beank.workFlowy.R.string as AppText


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginAccount: LoginAccount,
    private val initDataSetting: InitDataSetting,
    logRepository: LogRepository
) : WorkFlowyViewModel(logRepository){

    private val _inputEmail = MutableStateFlow("")
    private val _inputPassword = MutableStateFlow("")

    val inputEmail get() = _inputEmail.asStateFlow()
    val inputPassword get() = _inputPassword.asStateFlow()

    fun onEmailChange(newValue : String){
        _inputEmail.value = newValue
    }

    fun onPasswordChange(newValue: String){
        _inputPassword.value = newValue
    }

    fun initSetting(){
        launchCatching {
            initDataSetting()
        }
    }

    fun onSignInClick(openAndPopUp: (String, String) -> Unit) {
        if (!inputEmail.value.isValidEmail()) {
            SnackbarManager.showMessage(AppText.email_error)
            return
        }

        if (inputPassword.value.isBlank()) {
            SnackbarManager.showMessage(AppText.empty_password_error)
            return
        }

        if (!inputPassword.value.isValidPassword()) {
            SnackbarManager.showMessage(AppText.password_error)
            return
        }

        launchCatching {
            loginAccount(
                email = inputEmail.value,
                password = inputPassword.value,
                onSuccess = { openAndPopUp(NavigationItem.HOME.route, NavigationItem.LOGIN.route) },
                onFailMessage = {SnackbarManager.showMessage(AppText.login_server_error)}
            )
        }
    }

}