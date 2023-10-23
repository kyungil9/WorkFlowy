package com.beank.app.di

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.beank.app.service.GeofenceBroadcastReceiver
import com.beank.app.service.MessageWorker
import com.beank.data.di.ActivityPendingIntent
import com.beank.data.di.GeoPendingIntent
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.ActivityRecognitionClient
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.time.Duration
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlin.random.Random

@Module
@InstallIn(SingletonComponent::class)
object ManagerModule{

    @Singleton
    @Provides
    fun providesWorkManager(@ApplicationContext context: Context) : WorkManager {
        return WorkManager.getInstance(context)
    }
    @Singleton
    @Provides
    fun proviedsAlarmManager(@ApplicationContext context: Context) : AlarmManager {
        return context.getSystemService(AlarmManager::class.java)
    }

    @Singleton
    @Provides
    fun providesMessageRequest() : OneTimeWorkRequest.Builder {
        return OneTimeWorkRequestBuilder<MessageWorker>()
    }

    @Singleton
    @Provides
    fun providesActivityRecongnitionClient(@ApplicationContext context: Context) : ActivityRecognitionClient {
        return ActivityRecognition.getClient(context)
    }

    @Singleton
    @Provides
    fun providesGeoClient(@ApplicationContext context: Context) : GeofencingClient{
        return LocationServices.getGeofencingClient(context)
    }

    @Singleton
    @Provides
    @GeoPendingIntent
    fun providesGeofenceIntent(@ApplicationContext context: Context) : PendingIntent {
        val intent = Intent(context,GeofenceBroadcastReceiver::class.java)
        return PendingIntent.getBroadcast(context, 0,intent,PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
    }

    @Singleton
    @Provides
    @ActivityPendingIntent
    fun providesActivityIntent(@ApplicationContext context: Context) : PendingIntent {
        val intent = Intent(context,GeofenceBroadcastReceiver::class.java)
        return PendingIntent.getBroadcast(context, 1,intent,PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
    }
}