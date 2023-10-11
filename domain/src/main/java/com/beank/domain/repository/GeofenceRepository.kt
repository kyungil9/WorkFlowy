package com.beank.domain.repository

import com.beank.domain.model.FireStoreState
import com.beank.domain.model.GeofenceData
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import kotlinx.coroutines.flow.Flow

interface GeofenceRepository {

    suspend fun getChooseTrigger(id: String): String?

    fun getGeoTriggerList(): Flow<FireStoreState<List<GeofenceData>>>

    fun addGeofenceToClient(geofenceData: GeofenceData, onSuccess: () -> Unit, onFail: () -> Unit)

    fun addGeofenceListToClient(geofenceDataList: List<GeofenceData>, onSuccess: () -> Unit, onFail: () -> Unit)

    fun updateGeofenceToClient(geofenceData: GeofenceData,onSuccess: () -> Unit, onFail: () -> Unit)

    fun removeGeofenceToClient(id: String, onSuccess: () -> Unit, onFail: () -> Unit)
}