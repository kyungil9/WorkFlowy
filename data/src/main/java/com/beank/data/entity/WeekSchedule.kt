package com.beank.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import java.time.LocalDate
import java.time.LocalTime


data class WeekSchedule(
    @DocumentId var id : String? ="",
    var date : Int = 20220201,
    var startTime : String = "04:00",
    var endTime : String = "04:00",
    var icon : Int = 0,
    var title : String = "",
    var comment : String = ""
)