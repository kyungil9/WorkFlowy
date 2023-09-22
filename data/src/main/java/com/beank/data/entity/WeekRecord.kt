package com.beank.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import java.time.LocalDateTime

data class WeekRecord(
    @DocumentId var id : String? ="",
    val tag : String = "개인시간",
    var date : Int = 0,
    var startTime : Long = 0,
    var endTime: Long? = null,
    var progressTime : Long = 0,
    var pause : Boolean = false
)
