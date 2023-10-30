package com.beank.data.repositoryimpl

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.beank.data.datasource.StorageDataSource
import com.beank.data.di.ActivityPendingIntent
import com.beank.data.di.GeoPendingIntent
import com.beank.data.entity.WeekGeoTrigger
import com.beank.data.mapper.toGeofenceData
import com.beank.data.mapper.toWeekGeoTrigger
import com.beank.data.utils.dataStateObjects
import com.beank.data.utils.toObjects
import com.beank.domain.model.FireStoreState
import com.beank.domain.model.GeofenceData
import com.beank.domain.model.GeofenceEvent
import com.beank.domain.repository.GeofenceRepository
import com.google.android.gms.location.ActivityRecognitionClient
import com.google.android.gms.location.ActivityRecognitionResult
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionRequest
import com.google.android.gms.location.DetectedActivity
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class GeofenceRepositoryImpl @Inject constructor(
    private val geofencingClient: GeofencingClient,
    private val activityRecognitionClient: ActivityRecognitionClient,
    @GeoPendingIntent private val geoPendingIntent: PendingIntent,
    @ActivityPendingIntent private val activityPendingIntent: PendingIntent,
    private val storage : StorageDataSource
) : GeofenceRepository {

    private fun getGeofencingRequest(geofence: Geofence): GeofencingRequest =
        GeofencingRequest.Builder().setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL)
            .addGeofence(geofence).build()

    private fun getTempGeofencingRequest(geofence: Geofence): GeofencingRequest =
        GeofencingRequest.Builder().setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence).build()

    private fun getGeofencingRequest(geofenceList: List<Geofence>): GeofencingRequest =
        GeofencingRequest.Builder().setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL)
            .addGeofences(geofenceList).build()

    private fun createGeofence(geofenceData: GeofenceData): Geofence =
        Geofence.Builder()
            .setRequestId(geofenceData.id!!)
            .setCircularRegion(
                geofenceData.latitude,
                geofenceData.lonitude,
                geofenceData.radius
            )
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL or Geofence.GEOFENCE_TRANSITION_EXIT)
            .setLoiteringDelay(geofenceData.delayTime)
            .build()

    private fun createTempGeofence(geofenceData: GeofenceData): Geofence =
        Geofence.Builder()
            .setRequestId(geofenceData.id!!)
            .setCircularRegion(
                geofenceData.latitude,
                geofenceData.lonitude,
                geofenceData.radius
            )
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .setLoiteringDelay(geofenceData.delayTime)
            .build()

    override suspend fun getChooseTrigger(id: String): GeofenceData? =
        storage.store.document(storage.getUid()!!).collection(GEOTRIGGER).document(id).get()
            .await().toObject(WeekGeoTrigger::class.java)?.toGeofenceData()

    override fun getGeoTriggerList(): Flow<FireStoreState<List<GeofenceData>>> =
        storage.store.document(storage.getUid()!!).collection(GEOTRIGGER).dataStateObjects<WeekGeoTrigger,GeofenceData>()

    override suspend fun getTempGeoTrigger(): GeofenceData? =
        storage.store.document(storage.getUid()!!).collection(GEOTRIGGER).whereEqualTo("geoEvent",GeofenceEvent.TempRequest)
            .get().await().toObjects(WeekGeoTrigger::class.java,GeofenceData::class.java).firstOrNull()

    override suspend fun addGeofenceToClient(geofenceData: GeofenceData, onSuccess : () -> Unit, onFail : () -> Unit) {
        val id = storage.saveToReturnId(GEOTRIGGER,geofenceData.toWeekGeoTrigger())
        geofencingClient.addGeofences(getGeofencingRequest(createGeofence(geofenceData.copy(id = id))),geoPendingIntent).run {
            addOnSuccessListener { onSuccess() }
            addOnFailureListener { onFail() }
        }
    }

    override suspend fun addTempGeofenceToClient(geofenceData: GeofenceData, onSuccess : () -> Unit, onFail : () -> Unit) {
        val id = storage.saveToReturnId(GEOTRIGGER,geofenceData.toWeekGeoTrigger())
        geofencingClient.addGeofences(getTempGeofencingRequest(createTempGeofence(geofenceData.copy(id = id))),geoPendingIntent).run {
            addOnSuccessListener { onSuccess() }
            addOnFailureListener { onFail() }
        }
    }

    override suspend fun startGeofenceToClient(onSuccess : () -> Unit, onFail : () -> Unit) {
        val task = activityRecognitionClient.requestActivityTransitionUpdates(ActivityTransitionRequest(initTransitions()),activityPendingIntent)
        task.addOnSuccessListener {
            Log.d("walk","ok")
        }.addOnFailureListener {
            Log.e("walk",it.message.toString())
        }
        val geofenceDataList = storage.store.document(storage.getUid()!!).collection(GEOTRIGGER).get().await().toObjects(WeekGeoTrigger::class.java).map { it.toGeofenceData() }
        val geofenceList = ArrayList<Geofence>()
        geofenceDataList.forEach { geofenceData ->
            geofenceList.add(createGeofence(geofenceData))
        }
        if (geofenceList.isNotEmpty()){
            geofencingClient.addGeofences(getGeofencingRequest(geofenceList),geoPendingIntent).run {
                addOnSuccessListener { onSuccess() }
                addOnFailureListener { onFail() }
            }
        }
    }

    override fun updateGeofenceToClient(geofenceData: GeofenceData, onSuccess: () -> Unit, onFail: () -> Unit) {
        geofencingClient.removeGeofences(listOf(geofenceData.id)).run {
            addOnSuccessListener {
                geofencingClient.addGeofences(getGeofencingRequest(createGeofence(geofenceData)),geoPendingIntent).run {
                    addOnSuccessListener {
                        storage.replace(GEOTRIGGER,geofenceData.id!!,geofenceData.toWeekGeoTrigger())
                        onSuccess()
                    }
                    addOnFailureListener { onFail() }
                }
            }
            addOnFailureListener { onFail() }
        }
    }

    override fun removeGeofenceToClient(id: String, onSuccess : () -> Unit, onFail : () -> Unit) {
        storage.delete(GEOTRIGGER,id)
        geofencingClient.removeGeofences(listOf(id)).run {
            addOnSuccessListener { onSuccess() }
            addOnFailureListener { onFail() }
        }
    }

    override fun removeGeofenceToClient(onSuccess: () -> Unit, onFail: () -> Unit) {
        geofencingClient.removeGeofences(geoPendingIntent).run {
            addOnSuccessListener { onSuccess() }
            addOnFailureListener { onFail() }
        }
        activityRecognitionClient.removeActivityTransitionUpdates(activityPendingIntent)
    }

    private fun initTransitions() : List<ActivityTransition>{
        val transitions = mutableListOf<ActivityTransition>()
        transitions += ActivityTransition.Builder()
            .setActivityType(DetectedActivity.WALKING)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
            .build()
        transitions += ActivityTransition.Builder()
            .setActivityType(DetectedActivity.WALKING)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
            .build()
        transitions += ActivityTransition.Builder()
            .setActivityType(DetectedActivity.RUNNING)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
            .build()
        transitions += ActivityTransition.Builder()
            .setActivityType(DetectedActivity.RUNNING)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
            .build()
        transitions += ActivityTransition.Builder()
            .setActivityType(DetectedActivity.ON_BICYCLE)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
            .build()
        transitions += ActivityTransition.Builder()
            .setActivityType(DetectedActivity.ON_BICYCLE)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
            .build()
        transitions += ActivityTransition.Builder()
            .setActivityType(DetectedActivity.IN_VEHICLE)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
            .build()
        transitions += ActivityTransition.Builder()
            .setActivityType(DetectedActivity.IN_VEHICLE)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
            .build()
        return transitions
    }
    companion object{
        private const val GEOTRIGGER = "GeoTrigger"
    }
}