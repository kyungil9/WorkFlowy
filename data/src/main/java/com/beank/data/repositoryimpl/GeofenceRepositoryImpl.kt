package com.beank.data.repositoryimpl

import android.annotation.SuppressLint
import android.app.PendingIntent
import com.beank.data.datasource.StorageDataSource
import com.beank.data.entity.WeekGeoTrigger
import com.beank.data.utils.dataStateObjects
import com.beank.domain.model.FireStoreState
import com.beank.domain.model.GeofenceData
import com.beank.domain.model.GeofenceEvent
import com.beank.domain.repository.GeofenceRepository
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GeofenceRepositoryImpl @Inject constructor(
    private val geofencingClient: GeofencingClient,
    private val pendingIntent: PendingIntent,
    private val storage : StorageDataSource
) : GeofenceRepository {

    private fun getGeofencingRequest(geofence: Geofence): GeofencingRequest =
        GeofencingRequest.Builder().setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL)
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
            .setTransitionTypes(when(geofenceData.geoEvent){
                GeofenceEvent.EnterRequest -> Geofence.GEOFENCE_TRANSITION_DWELL
                GeofenceEvent.ExitRequest -> Geofence.GEOFENCE_TRANSITION_EXIT
                GeofenceEvent.EnterOrExitRequest -> Geofence.GEOFENCE_TRANSITION_DWELL or Geofence.GEOFENCE_TRANSITION_EXIT
                else -> Geofence.GEOFENCE_TRANSITION_DWELL
            })
            .setLoiteringDelay(geofenceData.delayTime)
            .build()

    override suspend fun getChooseTrigger(id: String): String? =
        storage.store.document(storage.getUid()!!).collection(GEOTRIGGER).document(id).get()
            .await().toObject(WeekGeoTrigger::class.java)?.id

    override fun getGeoTriggerList(): Flow<FireStoreState<List<GeofenceData>>> =
        storage.store.document(storage.getUid()!!).collection(GEOTRIGGER).dataStateObjects<WeekGeoTrigger,GeofenceData>()

    override fun addGeofenceToClient(geofenceData: GeofenceData, onSuccess : () -> Unit, onFail : () -> Unit) {
        storage.save(GEOTRIGGER,geofenceData)
        geofencingClient.addGeofences(getGeofencingRequest(createGeofence(geofenceData)),pendingIntent).run {
            addOnSuccessListener { onSuccess() }
            addOnFailureListener { onFail() }
        }
    }

    override fun addGeofenceListToClient(geofenceDataList: List<GeofenceData>, onSuccess : () -> Unit, onFail : () -> Unit) {
        val geofenceList = ArrayList<Geofence>()
        geofenceDataList.forEach { geofenceData ->
            geofenceList.add(createGeofence(geofenceData))
            storage.save(GEOTRIGGER,geofenceData)
        }
        geofencingClient.addGeofences(getGeofencingRequest(geofenceList),pendingIntent).run {
            addOnSuccessListener { onSuccess() }
            addOnFailureListener { onFail() }
        }
    }

    override fun updateGeofenceToClient(geofenceData: GeofenceData, onSuccess: () -> Unit, onFail: () -> Unit) {
        geofencingClient.removeGeofences(listOf(geofenceData.id)).run {
            addOnSuccessListener {
                geofencingClient.addGeofences(getGeofencingRequest(createGeofence(geofenceData)),pendingIntent).run {
                    addOnSuccessListener {
                        storage.replace(GEOTRIGGER,geofenceData.id!!,geofenceData)
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
    companion object{
        private const val GEOTRIGGER = "GeoTrigger"
    }
}