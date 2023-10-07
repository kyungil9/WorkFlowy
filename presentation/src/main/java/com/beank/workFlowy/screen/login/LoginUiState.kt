package com.beank.workFlowy.screen.login

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
@Stable
class LoginUiState(
    email : String = "",
    password : String = ""
){
    var email by mutableStateOf(email)
    var password by mutableStateOf(password)
}