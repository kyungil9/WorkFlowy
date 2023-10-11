package com.beank.data.entity

import com.beank.domain.model.GeofenceEvent
import com.google.firebase.firestore.DocumentId
import java.time.LocalTime

data class WeekGeoTrigger(
    @DocumentId val id : String? = null,
    val tag : String = "",
    val latitude : Double = 0.0,
    val lonitude : Double = 0.0,
    val radius : Float = 100f,
    val delayTime : Int = 300000,
    var timeOption : Boolean = false,
    var startTime : String = "00:00",
    var endTime : String = "00:00",
    val geoEvent : Int = GeofenceEvent.EnterRequest
)
