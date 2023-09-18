package com.beank.domain.model

import java.time.LocalDateTime

data class Record(
    var id : String? =null,
    val tag : String = "",
    var startTime : LocalDateTime = LocalDateTime.now(),
    var endTime: LocalDateTime? = null,
    var progressTime : Long = 0,
    var pause : Boolean = true
)
