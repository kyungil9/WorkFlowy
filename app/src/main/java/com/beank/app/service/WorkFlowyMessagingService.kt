package com.beank.app.service

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.beank.app.utils.notificationBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class WorkFlowyMessagingService : FirebaseMessagingService() {
    @Inject lateinit var notification: NotificationManagerCompat //???
    private val random = Random

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let { message ->
            sendNotification(message)
        }
    }

    private fun sendNotification(message: RemoteMessage.Notification) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        ) {
            notification.notify(random.nextInt(), notificationBuilder(this,message.title,message.body).build())
        }
    }

    override fun onNewToken(token: String) {

    }

    companion object {
        const val CHANNEL_NAME = "FCM notification channel"
        const val TOKEN = "Token"
    }
}