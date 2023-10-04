package com.beank.domain.usecase

import com.beank.domain.usecase.user.GetUserInfo
import com.beank.domain.usecase.user.UpdateUserGrade
import com.beank.domain.usecase.user.UpdateUserNickName
import com.beank.domain.usecase.user.UploadImageUrl

data class UserUsecases(
    val getUserInfo: GetUserInfo,
    val updateUserNickName: UpdateUserNickName,
    val updateUserGrade: UpdateUserGrade,
    val uploadImageUrl: UploadImageUrl
)
