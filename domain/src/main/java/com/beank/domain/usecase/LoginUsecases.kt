package com.beank.domain.usecase

import com.beank.domain.usecase.account.LoginAccount
import com.beank.domain.usecase.message.InsertToken
import com.beank.domain.usecase.tag.InitDataSetting

data class LoginUsecases(
    val loginAccount: LoginAccount,
    val initDataSetting: InitDataSetting,
    val insertToken : InsertToken
)