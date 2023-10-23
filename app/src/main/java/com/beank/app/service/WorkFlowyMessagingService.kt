package com.beank.app.service

import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.beank.domain.usecase.message.NewToken
import com.beank.workFlowy.utils.MessageMode
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@AndroidEntryPoint
class WorkFlowyMessagingService : FirebaseMessagingService() {
    @Inject lateinit var workManager: WorkManager
    @Inject lateinit var message : FirebaseMessaging
    @Inject lateinit var newToken: NewToken

    override fun onMessageReceived(remoteMessage: RemoteMessage){
        remoteMessage.notification?.let { message ->
            Log.d("remoteMessage",message.title!!)
            val messageWorkRequest = OneTimeWorkRequestBuilder<MessageWorker>()
                .setInputData(
                    workDataOf(
                    "mode" to MessageMode.REMOTE,
                    "title" to message.title,
                    "body" to message.body)
                )
                .setBackoffCriteria(BackoffPolicy.LINEAR,30000, TimeUnit.MILLISECONDS)
                .build()
            workManager.enqueue(messageWorkRequest)
        }
    }

    override fun onNewToken(token: String) {
        newToken(token)
    }

    companion object {
        const val CHANNEL_NAME = "FCM notification channel"
    }
}