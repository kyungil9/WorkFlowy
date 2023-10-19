package com.beank.app.service

import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class WorkFlowyMessagingService : FirebaseMessagingService() {
    @Inject lateinit var workManager: WorkManager
    override fun onMessageReceived(remoteMessage: RemoteMessage){
        remoteMessage.notification?.let { message ->
            val messageWorkRequest = OneTimeWorkRequestBuilder<MessageWorker>()
                .setInputData(
                    workDataOf(
                    "remote" to true,
                    "title" to message.title,
                    "body" to message.body)
                )
                .build()
            workManager.enqueue(messageWorkRequest)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    companion object {
        const val CHANNEL_NAME = "FCM notification channel"
    }
}