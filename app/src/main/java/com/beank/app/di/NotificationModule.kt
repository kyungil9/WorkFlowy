package com.beank.app.di

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.beank.app.service.RecordMessageReceiver
import com.beank.app.service.RecordService
import com.beank.app.service.WorkFlowyMessagingService
import com.beank.workFlowy.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {
    @Singleton
    @Provides
    @NormalNotification
    fun providesNotificationManager(@ApplicationContext context : Context) : NotificationManagerCompat {
        val manager = NotificationManagerCompat.from(context)
        val channel = NotificationChannel(context.getString(R.string.default_notification_channel_id), WorkFlowyMessagingService.CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        manager.createNotificationChannel(channel)
        return manager
    }

    @Singleton
    @Provides
    @RecordNotification
    fun providesRecordNotificationManager(@ApplicationContext context : Context) : NotificationManagerCompat {
        val manager = NotificationManagerCompat.from(context)
        val channel = NotificationChannel(context.getString(R.string.record_channel_id), "Record_Channel", NotificationManager.IMPORTANCE_LOW)
        manager.createNotificationChannel(channel)
        return manager
    }

    @Singleton
    @Provides
    fun providesRecordMessageReceiver() : RecordMessageReceiver = RecordMessageReceiver()

    @Singleton
    @Provides
    fun providesRecordIntent(@ApplicationContext context: Context) : Intent = Intent(context,RecordService::class.java)

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NormalNotification

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RecordNotification