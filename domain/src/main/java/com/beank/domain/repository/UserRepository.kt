package com.beank.domain.repository

import android.net.Uri
import com.beank.domain.model.FireStoreState
import com.beank.domain.model.UserInfo
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun initUserInfo()

    fun getUserInfo() : Flow<FireStoreState<List<UserInfo>>>

    fun uploadImageUrl(uri: Uri,onFail : () -> Unit) //이미지 업로드후 uri받아서 db저장

    fun updateUserNickName(name : String)

    fun updateUserGrade(grade : Int)

}