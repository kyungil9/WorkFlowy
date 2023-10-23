package com.beank.app.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.BackoffPolicy
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
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class GeofenceBroadcastReceiver : BroadcastReceiver(){
    @Inject lateinit var workManager: WorkManager

    override fun onReceive(context : Context?, intented : Intent?) {
        intented?.let { intent ->
            Log.d("trigger",intent.toString())
            if (intent.action == "android.intent.action.BOOT_COMPLETED" || intent.action == "android.intent.action.LOCKED_BOOT_COMPLETED"){
                //지오펜스 재등록 기능 추가
                val geoWorkRequest = OneTimeWorkRequestBuilder<RecordWorker>()
                    .setInputData(workDataOf(
                        "reboot" to true
                    ))
                    .setBackoffCriteria(BackoffPolicy.LINEAR,30000, TimeUnit.MILLISECONDS)
                    .build()
                workManager.enqueue(geoWorkRequest)
            }else{
                val geofencingEvent = GeofencingEvent.fromIntent(intent)
                val activityEvent = ActivityTransitionResult.hasResult(intent)
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
                            .setBackoffCriteria(BackoffPolicy.LINEAR,30000,TimeUnit.MILLISECONDS)
                            .build()
                        workManager.enqueue(geoWorkRequest)
                        Log.e(TAG, "success geowork")

                    }else {
                        //error
                        Log.e(TAG, "error code 1")
                    }
                }else{
                    if (activityEvent){
                        val result = ActivityTransitionResult.extractResult(intent)!!
                        val event = result.transitionEvents.first()
                        if (event.activityType == DetectedActivity.ON_FOOT || event.activityType == DetectedActivity.IN_VEHICLE ||
                            event.activityType == DetectedActivity.ON_BICYCLE){
                            if (event.transitionType == ActivityTransition.ACTIVITY_TRANSITION_ENTER){
                                val activityWorkRequest = OneTimeWorkRequestBuilder<RecordWorker>()
                                    .setInputData(workDataOf(
                                        "activity" to event.transitionType,
                                        "dateTime" to LocalDateTime.now().toLong()))
                                    .setBackoffCriteria(BackoffPolicy.LINEAR,30000,TimeUnit.MILLISECONDS)
                                    .build()
                                workManager.enqueue(activityWorkRequest)
                            }else if (event.transitionType == ActivityTransition.ACTIVITY_TRANSITION_EXIT){
                                val activityWorkRequest = OneTimeWorkRequestBuilder<RecordWorker>()
                                    .setInputData(workDataOf(
                                        "activity" to 0,
                                        "dateTime" to LocalDateTime.now().toLong()))
                                    .setBackoffCriteria(BackoffPolicy.LINEAR,30000,TimeUnit.MILLISECONDS)
                                    .build()
                                workManager.enqueue(activityWorkRequest)
                            }
                        }
                    }
                    return
                }
            }
        }
    }

    companion object{
        private const val TAG = "GeofenceError"
    }
}

