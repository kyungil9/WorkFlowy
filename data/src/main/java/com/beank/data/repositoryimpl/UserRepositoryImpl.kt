package com.beank.data.repositoryimpl

import android.net.Uri
import com.beank.data.datasource.StorageDataSource
import com.beank.data.entity.WeekUserInfo
import com.beank.data.utils.dataStateObject
import com.beank.data.utils.dataStateObjects
import com.beank.domain.model.FireStoreState
import com.beank.domain.model.UserInfo
import com.beank.domain.repository.UserRepository
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val storage : StorageReference,
    private val store : StorageDataSource
) : UserRepository {

    override fun initUserInfo() {
        store.replace(USERPROFILE, USER,WeekUserInfo(nickname = "anonymous"))
    }

    override fun getUserInfo(): Flow<FireStoreState<List<UserInfo>>> =
        store.store.document(store.getUid()!!).collection(USERPROFILE)
            .dataStateObjects<WeekUserInfo,UserInfo>()

    override fun updateUserGrade(grade: Int) {
        store.update(USERPROFILE, USER, mapOf("grade" to grade))
    }

    override fun updateUserNickName(name: String) {
        store.update(USERPROFILE, USER, mapOf("nickname" to name))
    }

    override fun uploadImageUrl(uri: Uri, onFail : () -> Unit) {
        val imageRef = storage.child("userImage/${store.getUid()!!}.jpg")
        val uploadTask = imageRef.putFile(uri)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful){
                task.exception?.let {
                    throw it
                }
            }
            imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful){
                store.update(USERPROFILE, USER, mapOf("urlImage" to task.result.toString()))//db저장
            }else{
                onFail()
            }
        }

    }

    companion object{
        private const val USERPROFILE = "UserProfile"
        private const val USER = "User"
    }

}