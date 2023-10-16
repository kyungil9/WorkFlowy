package com.beank.workFlowy.utils

import android.location.Geocoder
import android.os.Build
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