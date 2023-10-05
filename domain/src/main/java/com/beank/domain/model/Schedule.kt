package com.beank.domain.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
data class Schedule(
    var id: String? = null,
    var date: LocalDate = LocalDate.now(),
    var startTime: LocalTime = LocalTime.now(),
    var endTime: LocalTime = LocalTime.now(),
    var time : Boolean = false,
    var icon: Int = 0,
    var title: String = "",
    var comment: String = "",
    var check : Boolean = false,
    var alarm : Boolean = false,
    var alarmTime : LocalDateTime = LocalDateTime.now(),
    var alarmState : String = "5분전"
)
