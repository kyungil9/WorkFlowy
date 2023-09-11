package com.beank.domain.model

import java.time.LocalDateTime

data class Record(
    var id : Int? = 0,
    val tag : String,
    var startTime : LocalDateTime,
    var endTime: LocalDateTime?,
    var progressTime : Long,
    var pause : Boolean
)
