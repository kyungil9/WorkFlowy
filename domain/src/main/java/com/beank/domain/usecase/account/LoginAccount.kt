package com.beank.domain.usecase.account

import com.beank.domain.repository.AccountRepository
import javax.inject.Inject
import javax.inject.Singleton

class LoginAccount @Inject constructor(
    private val accountRepository: AccountRepository
){
    operator fun invoke(email : String, password : String, onSuccess : () -> Unit, onFailMessage : () -> Unit) =
        accountRepository.loginAccount(email, password, onSuccess, onFailMessage)
}