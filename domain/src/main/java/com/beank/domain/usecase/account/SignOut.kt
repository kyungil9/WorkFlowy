package com.beank.domain.usecase.account

import com.beank.domain.repository.AccountRepository
import javax.inject.Inject

class SignOut @Inject constructor(
    private val accountRepository: AccountRepository
){
    operator fun invoke() = accountRepository.signOut()
}