package com.beank.workFlowy.utils

import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.beank.domain.model.GeofenceEvent
import java.io.IOException

fun Geocoder.getAddress(
    name : String,
    address : (android.location.Address?) -> Unit
){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
        getFromLocationName(name,1){
            address(it.firstOrNull())
        }
        return
    }
    try {
        address(getFromLocationName(name,1)?.firstOrNull())
    }catch (e: IOException){
        address(null)
        e.printStackTrace()
    }
}

fun Int.exchangeTime() : String{
    return when(this){
        300000 -> "5분"
        600000 -> "10분"
        900000 -> "15분"
        1800000 -> "30분"
        else -> "5분"
    }
}

fun Int.exchangeEvent() : String{
    return when(this){
        GeofenceEvent.EnterRequest -> "Enter"
        GeofenceEvent.ExitRequest -> "Exit"
        GeofenceEvent.EnterOrExitRequest -> "Enter/Exit"
        else -> "Enter"
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun Context.hasNotificationPermissions() = ContextCompat.checkSelfPermission(
        this,
        android.Manifest.permission.POST_NOTIFICATIONS
    ) == PackageManager.PERMISSION_GRANTED


object MessageMode {
    const val REMOTE = 1
    const val LOCAL = 2
    const val REBOOT = 3
    const val NOW = 4
    const val CANCLE = 5
    const val CANCLEALL = 6
    const val TODAY = 7
    const val REPEAT = 8
}

object RecordMode {
    const val START = 1
    const val STOP = 2
    const val REBOOT = 3
    const val NEXT_RECORD = 4
}
