package com.beank.workFlowy.utils

import android.content.res.TypedArray

fun imageToInt(image : Int, typedList : TypedArray) : Int {
    var num = 0
    for (i in 0 until typedList.length()){
        if (image == typedList.getResourceId(i,0)){
            num = i
            break
        }
    }
    return num
}

fun intToImage(num : Int, typedList : TypedArray) : Int = typedList.getResourceId(num,0)