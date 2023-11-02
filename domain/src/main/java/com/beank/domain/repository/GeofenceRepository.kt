package com.beank.domain.repository

import com.beank.domain.model.FireStoreState
import com.beank.domain.model.GeofenceData
import kotlinx.coroutines.flow.Flow

interface GeofenceRepository {

    suspend fun getChooseTrigger(id: String): GeofenceData?

    fun getGeoTriggerList(): Flow<FireStoreState<List<GeofenceData>>>

    suspend fun getTempGeoTrigger() : GeofenceData?

    suspend fun addTempGeofenceToClient(geofenceData: GeofenceData, onSuccess: () -> Unit, onFail: () -> Unit)

    suspend fun addGeofenceToClient(geofenceData: GeofenceData, onSuccess: () -> Unit, onFail: () -> Unit)

    suspend fun startGeofenceToClient(onSuccess : () -> Unit, onFail : () -> Unit)

    suspend fun startMoveToClient(onSuccess : () -> Unit, onFail : () -> Unit)

    fun updateGeofenceToClient(geofenceData: GeofenceData,onSuccess: () -> Unit, onFail: () -> Unit)

    fun removeGeofenceToClient(id: String, onSuccess: () -> Unit, onFail: () -> Unit)

    fun removeGeofenceToClient(onSuccess: () -> Unit, onFail: () -> Unit)

    fun removeMoveToClient(onSuccess: () -> Unit, onFail: () -> Unit)
}