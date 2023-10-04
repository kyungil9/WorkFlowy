package com.beank.data.entity

import com.google.firebase.firestore.DocumentId

data class WeekRecord(
    @DocumentId var id : String? = null,
    val tag : String = "개인시간",
    var date : Int = 0,
    var startTime : Long = 0,
    var endTime: Long? = null,
    var progressTime : Long = 0,
    var pause : Boolean = false
)
