package com.beank.workFlowy.screen.trigger_setting

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.beank.domain.model.GeofenceData
import com.beank.domain.model.GeofenceEvent
import com.beank.domain.model.Tag
import com.beank.domain.model.onEmpty
import com.beank.domain.model.onException
import com.beank.domain.model.onSuccess
import com.beank.domain.repository.LogRepository
import com.beank.domain.usecase.TriggerSettingUsecases
import com.beank.presentation.R
import com.beank.workFlowy.component.snackbar.SnackbarManager
import com.beank.workFlowy.screen.WorkFlowyViewModel
import com.beank.workFlowy.utils.fromGeofenceJson
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalTime
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class TriggerSettingViewModel @Inject constructor(
    private val savedStateHandle : SavedStateHandle,
    private val triggerSettingUsecases: TriggerSettingUsecases,
    logRepository: LogRepository
) : WorkFlowyViewModel(logRepository){

    val uiState = TriggerSettingUiState()

    init {
        getAllTagInfo()
        val geofenceData = savedStateHandle.get<String>("geo")
        if (!geofenceData.isNullOrEmpty()){
            val geofence = geofenceData.fromGeofenceJson()
            geofence.let { geo ->
                uiState.id = geo.id ?: ""
                uiState.tag = geo.tag
                uiState.tagImage = geo.tagImage
                uiState.latitude = geo.latitude
                uiState.lonitude = geo.lonitude
                uiState.radius = geo.radius.toDouble()
                uiState.delayTime = geo.delayTime
                uiState.timeOption = geo.timeOption
                uiState.startTime = geo.startTime
                uiState.endTime = geo.endTime
                uiState.geoEvent = geo.geoEvent
            }
        }
    }

    fun onTagSelect(tag: Tag){
        uiState.tag = tag.title
        uiState.tagImage = tag.icon
    }

    fun onLatLngUpdate(latLng: LatLng, radius : Double = 30.0){
        uiState.latitude = latLng.latitude
        uiState.lonitude = latLng.longitude
        uiState.radius = radius
    }

    fun onTimeOptionUpdate(value : Boolean){
        uiState.timeOption = value
    }

    fun onGeoEventUpdate(trigger : String){
        uiState.geoEvent = when(trigger){
            "Enter" -> GeofenceEvent.EnterRequest
            "Exit" -> GeofenceEvent.ExitRequest
            "Enter/Exit" -> GeofenceEvent.EnterOrExitRequest
            else -> GeofenceEvent.EnterRequest
        }
    }

    fun onDelayTimeUpdate(time : String){
        uiState.delayTime = when(time){
            "5분" -> 300000
            "10분" -> 600000
            "15분" -> 900000
            "30분" -> 1800000
            else -> 300000
        }
    }

    fun onTimeChange(startHour : Int, startMinute : Int, endHour : Int, endMinute : Int) : Boolean{
        return if (startHour > endHour || (startHour == endHour && startMinute > endMinute)){
            false
        }else{
            uiState.startTime = LocalTime.of(startHour, startMinute)
            uiState.endTime = LocalTime.of(endHour, endMinute)
            true
        }
    }

    fun onTriggerAdd(){
        launchCatching {
            triggerSettingUsecases.addGeofence(GeofenceData(
                id = null,
                tag  = uiState.tag,
                tagImage  = uiState.tagImage,
                latitude  = uiState.latitude,
                lonitude  = uiState.lonitude,
                radius = uiState.radius.toFloat(),
                delayTime  = uiState.delayTime,
                timeOption  = uiState.timeOption,
                startTime  = uiState.startTime,
                endTime = uiState.endTime,
                geoEvent = uiState.geoEvent
            ))
        }
    }

    fun onTriggerUpdate(){
        launchCatching {
            triggerSettingUsecases.updateGeofence(
                GeofenceData(
                    id = uiState.id,
                    tag  = uiState.tag,
                    tagImage  = uiState.tagImage,
                    latitude  = uiState.latitude,
                    lonitude  = uiState.lonitude,
                    radius = uiState.radius.toFloat(),
                    delayTime  = uiState.delayTime,
                    timeOption  = uiState.timeOption,
                    startTime  = uiState.startTime,
                    endTime = uiState.endTime,
                    geoEvent = uiState.geoEvent
                )
            )
        }
    }

    private fun getAllTagInfo() = triggerSettingUsecases.getAllTag()
        .flowOn(Dispatchers.IO).onEach { state ->
            state.onEmpty {
                uiState.tagList = emptyList()
            }
            state.onSuccess { tagList ->
                uiState.tagList = tagList
            }
            state.onException { message, e ->
                e.message?.let {
                    if (!it.contains("PERMISSION_DENIED")){
                        SnackbarManager.showMessage(R.string.firebase_server_error)
                        logFirebaseFatalCrash(message, e)
                    }
                }
            }
        }.launchIn(viewModelScope)

}