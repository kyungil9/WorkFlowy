package com.beank.data.repositoryimpl

import android.util.Log
import androidx.test.espresso.remote.EspressoRemoteMessage.To
import com.beank.data.datasource.StorageDataSource
import com.beank.domain.repository.MessageRepository
import com.google.firebase.messaging.FirebaseMessaging
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val storage: StorageDataSource,
    private val message : FirebaseMessaging
) : MessageRepository {

    override fun insertToken() {
        message.token.addOnCompleteListener { task ->
            if (task.isSuccessful){
                storage.replace(TOKEN, TOKEN ,mapOf("token" to task.result))
            }
        }
    }

    companion object{
        private val TOKEN = "Token"
    }
}