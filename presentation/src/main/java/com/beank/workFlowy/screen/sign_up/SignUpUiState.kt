package com.beank.workFlowy.screen.sign_up

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Stable
class SignUpUiState(
    email : String = "",
    password : String = "",
    repeatPassword : String = ""
){
    var email by mutableStateOf(email)
    var password by mutableStateOf(password)
    var repeatPassword by mutableStateOf(repeatPassword)
}
