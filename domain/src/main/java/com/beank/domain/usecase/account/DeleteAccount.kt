package com.beank.domain.usecase.account

import com.beank.domain.repository.AccountRepository
import javax.inject.Inject

class DeleteAccount @Inject constructor(
    private val accountRepository: AccountRepository
){
    operator fun invoke( onSuccess : () -> Unit, onFailMessage : () -> Unit) =
        accountRepository.deleteAccount(onSuccess, onFailMessage)
}