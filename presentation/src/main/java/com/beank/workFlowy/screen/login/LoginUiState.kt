package com.beank.workFlowy.screen.login

import androidx.compose.runtime.Stable

@Stable
data class LoginUiState(
    val email : String = "",
    val password : String = ""
)
