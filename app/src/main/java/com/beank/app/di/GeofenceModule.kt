package com.beank.app.di

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.WorkManager
import com.beank.app.service.GeofenceBroadcastReceiver
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.ActivityRecognitionClient
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GeofenceModule{

    @Singleton
    @Provides
    fun providesWorkManager(@ApplicationContext context: Context) : WorkManager {
        return WorkManager.getInstance(context)
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
    fun providesGeofenceIntent(@ApplicationContext context: Context) : PendingIntent {
        val intent = Intent(context,GeofenceBroadcastReceiver::class.java)
        return PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
    }

}