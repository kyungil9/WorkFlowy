package com.beank.domain.usecase

import com.beank.domain.usecase.account.LoginAccount
import com.beank.domain.usecase.message.InsertToken
import com.beank.domain.usecase.message.SubscribeNotice
import com.beank.domain.usecase.setting.InitSetting
import com.beank.domain.usecase.tag.InitDataSetting

data class LoginUsecases(
    val loginAccount: LoginAccount,
    val initDataSetting: InitDataSetting,
    val insertToken : InsertToken,
    val initSetting: InitSetting,
    val subscribeNotice: SubscribeNotice
)