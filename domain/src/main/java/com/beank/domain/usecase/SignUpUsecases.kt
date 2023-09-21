package com.beank.domain.usecase

import com.beank.domain.usecase.account.CreateAccount
import com.beank.domain.usecase.tag.InitDataSetting

data class SignUpUsecases(
    val createAccount: CreateAccount,
    val initDataSetting: InitDataSetting
)
