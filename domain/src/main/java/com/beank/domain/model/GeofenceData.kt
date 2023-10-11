package com.beank.domain.model

import android.os.Build
import android.window.SplashScreen.OnExitAnimationListener
import androidx.annotation.RequiresApi
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
data class GeofenceData(
    var id : String? = null,
    var tag : String = "",
    var latitude : Double = 0.0,
    var lonitude : Double = 0.0,
    var radius : Float = 100f,
    var delayTime : Int = 300000,
    var timeOption : Boolean = false,
    var startTime : LocalTime = LocalTime.now(),
    var endTime : LocalTime = LocalTime.now(),
    var geoEvent : Int = GeofenceEvent.EnterRequest
)

object GeofenceEvent{
    const val EnterRequest = 0
    const val ExitRequest = 1
    const val EnterOrExitRequest = 2
}
