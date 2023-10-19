package com.beank.app.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.beank.workFlowy.utils.toLong
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionResult
import com.google.android.gms.location.DetectedActivity
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class GeofenceBroadcastReceiver : BroadcastReceiver(){
    @Inject lateinit var workManager: WorkManager

    override fun onReceive(context : Context?, intent : Intent?) {
        val geofencingEvent = intent?.let { GeofencingEvent.fromIntent(it) }
        val activityEvent = intent?.let { ActivityTransitionResult.hasResult(intent) }
        if (geofencingEvent != null) {
            if (geofencingEvent.hasError()) {
                val errorMessage = GeofenceStatusCodes
                    .getStatusCodeString(geofencingEvent.errorCode)
                Log.e(TAG, errorMessage)
                return
            }
            val geofenceTransition = geofencingEvent?.geofenceTransition!!
            if ((geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL)or(geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT)) {
                val triggeringGeofences = geofencingEvent.triggeringGeofences!!
                val geoWorkRequest = OneTimeWorkRequestBuilder<RecordWorker>()
                    .setInputData(workDataOf(
                        "geofenceId" to triggeringGeofences.last().requestId,
                        "geoState" to geofenceTransition,
                        "dateTime" to LocalDateTime.now().toLong()))
                    .build()
                workManager.enqueue(geoWorkRequest)

            }else {
                //error
                Log.e(TAG, "error code 1")
            }
        }else{
            if (activityEvent != null){
                if (activityEvent){
                    val result = ActivityTransitionResult.extractResult(intent)!!
                    val event = result.transitionEvents.last()
                        if (event.activityType == DetectedActivity.WALKING || event.activityType == DetectedActivity.IN_VEHICLE ||
                            event.activityType == DetectedActivity.ON_BICYCLE || event.activityType == DetectedActivity.RUNNING){
                            if (event.transitionType == ActivityTransition.ACTIVITY_TRANSITION_ENTER){
                                val activityWorkRequest = OneTimeWorkRequestBuilder<RecordWorker>()
                                    .setInputData(workDataOf(
                                        "activity" to event.transitionType,
                                        "dateTime" to LocalDateTime.now().toLong()))
                                    .build()
                                workManager.enqueue(activityWorkRequest)
                            }else if (event.transitionType == ActivityTransition.ACTIVITY_TRANSITION_EXIT){
                                val activityWorkRequest = OneTimeWorkRequestBuilder<RecordWorker>()
                                    .setInputData(workDataOf(
                                        "activity" to "Exit",
                                        "dateTime" to LocalDateTime.now().toLong()))
                                    .build()
                                workManager.enqueue(activityWorkRequest)
                            }
                        }

                }
            }
            return
        }
    }

    companion object{
        private const val TAG = "GeofenceError"
    }
}

