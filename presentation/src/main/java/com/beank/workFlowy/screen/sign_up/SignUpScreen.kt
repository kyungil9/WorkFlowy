package com.beank.workFlowy.screen.sign_up

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.beank.presentation.R
import com.beank.workFlowy.component.BasicButton
import com.beank.workFlowy.component.EmailField
import com.beank.workFlowy.component.PasswordField
import com.beank.workFlowy.component.RepeatPasswordField
import com.beank.workFlowy.component.basicButton
import com.beank.workFlowy.component.fieldModifier

@RequiresApi(Build.VERSION_CODES.FROYO)
@Composable
fun SignUpScreen(
    signUpViewModel: SignUpViewModel = hiltViewModel(),
    onBack : () -> Unit
){
    val uiState = signUpViewModel.uiState
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        EmailField(value = uiState.email, onNewValue = signUpViewModel::onEmailChange, Modifier.fieldModifier())
        PasswordField(value = uiState.password, onNewValue = signUpViewModel::onPasswordChange, Modifier.fieldModifier())
        RepeatPasswordField(value = uiState.repeatPassword, onNewValue = signUpViewModel::onRepeatPasswordChange, Modifier.fieldModifier())

        BasicButton(text = R.string.create_account, modifier = Modifier.basicButton()) {
            signUpViewModel.onSignInClick(onBack)
        }

    }

}