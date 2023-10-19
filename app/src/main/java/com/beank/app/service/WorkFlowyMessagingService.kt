package com.beank.app.service

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.beank.app.utils.notificationBuilder
import com.beank.domain.usecase.setting.GetNoticeAlarm
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class WorkFlowyMessagingService : FirebaseMessagingService() {
    @Inject lateinit var notification: NotificationManagerCompat //???
    @Inject lateinit var getNoticeAlarm: GetNoticeAlarm
    private val random = Random
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    override fun onMessageReceived(remoteMessage: RemoteMessage){
        remoteMessage.notification?.let { message ->
            serviceScope.launch {
                if (getNoticeAlarm().first()){//??
                    sendNotification(message)
                }
            }
        }
    }

    private fun sendNotification(message: RemoteMessage.Notification) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notification.notify(random.nextInt(), notificationBuilder(this,message.title,message.body).build())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }
    companion object {
        const val CHANNEL_NAME = "FCM notification channel"
        const val TOKEN = "Token"
    }
}