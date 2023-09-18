package com.beank.domain.model

import java.time.LocalDate
import java.time.LocalTime

data class Schedule(
    var id : String? = null,
    var date : LocalDate = LocalDate.now(),
    var startTime : LocalTime = LocalTime.now(),
    var endTime : LocalTime = LocalTime.now(),
    var icon : Int = 0,
    var title : String = "",
    var comment : String = ""
)
