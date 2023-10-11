package com.beank.app.di

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import com.beank.app.service.WorkFlowyMessagingService
import com.beank.workFlowy.R
import com.google.firebase.messaging.FirebaseMessagingService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.internal.managers.ApplicationComponentManager
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {
    @Singleton
    @Provides
    fun providesNotificationManager(@ApplicationContext context : Context) : NotificationManagerCompat {
        val manager = NotificationManagerCompat.from(context)
        val channel = NotificationChannel(context.getString(R.string.default_notification_channel_id), WorkFlowyMessagingService.CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        manager.createNotificationChannel(channel)
        return manager
    }
}