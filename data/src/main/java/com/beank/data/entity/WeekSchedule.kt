package com.beank.data.entity

import com.google.firebase.firestore.DocumentId


data class WeekSchedule(
    @DocumentId var id : String? = null,
    var date : Int = 20220201,
    var startTime : String = "04:00",
    var endTime : String = "04:00",
    var time : Boolean = false,
    var icon : Int = 0,
    var title : String = "",
    var comment : String = "",
    var check : Boolean = false,
    var alarm : Boolean = false,
    var alarmTime : Long = 0,
    var alarmState : String = "5분전"
)
