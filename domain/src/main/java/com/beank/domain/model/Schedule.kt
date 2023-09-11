package com.beank.domain.model

import java.time.LocalDate
import java.time.LocalTime

data class Schedule(
    var id : Int?,
    var date : LocalDate,
    var startTime : LocalTime,
    var endTime : LocalTime,
    var icon : Int,
    var title : String,
    var comment : String
)
