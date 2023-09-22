package com.beank.workFlowy.screen.sign_up

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.beank.workFlowy.R
import com.beank.workFlowy.component.BasicButton
import com.beank.workFlowy.component.EmailField
import com.beank.workFlowy.component.PasswordField
import com.beank.workFlowy.component.RepeatPasswordField
import com.beank.workFlowy.component.basicButton
import com.beank.workFlowy.component.fieldModifier

@Composable
fun SignUpScreen(
    signUpViewModel: SignUpViewModel = hiltViewModel(),
    onBack : () -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        EmailField(value = signUpViewModel.inputEmail, onNewValue = signUpViewModel::onEmailChange, Modifier.fieldModifier())
        PasswordField(value = signUpViewModel.inputPassword, onNewValue = signUpViewModel::onPasswordChange, Modifier.fieldModifier())
        RepeatPasswordField(value = signUpViewModel.inputRepeatPassword, onNewValue = signUpViewModel::onRepeatPassword, Modifier.fieldModifier())

        BasicButton(text = R.string.create_account, modifier = Modifier.basicButton()) {
            signUpViewModel.onSignInClick(onBack)
        }

    }

}