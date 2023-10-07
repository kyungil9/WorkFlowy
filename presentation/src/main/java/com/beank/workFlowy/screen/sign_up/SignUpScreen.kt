package com.beank.workFlowy.screen.sign_up

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.beank.presentation.R
import com.beank.workFlowy.component.BasicButton
import com.beank.workFlowy.component.EmailField
import com.beank.workFlowy.component.PasswordField
import com.beank.workFlowy.component.RepeatPasswordField
import com.beank.workFlowy.component.WeekLayout
import com.beank.workFlowy.component.basicButton
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SignUpScreen(
    signUpViewModel: SignUpViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    onBack : () -> Unit
){
    val uiState = signUpViewModel.uiState

    WeekLayout(snackbarHostState = snackbarHostState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            EmailField(value = {uiState.email}, onNewValue = signUpViewModel::onEmailChange)
            PasswordField(value = {uiState.password}, onNewValue = signUpViewModel::onPasswordChange)
            RepeatPasswordField(value = {uiState.repeatPassword}, onNewValue = signUpViewModel::onRepeatPasswordChange)

            BasicButton(text = R.string.create_account, modifier = Modifier.basicButton()) {
                signUpViewModel.onSignInClick(onBack)
            }

        }
    }
}