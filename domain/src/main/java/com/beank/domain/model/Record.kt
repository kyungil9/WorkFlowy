package com.beank.domain.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
@RequiresApi(Build.VERSION_CODES.O)
data class Record(
    var id : String? =null,
    val tag : String = "",
    var date : LocalDate = LocalDate.now(),
    var startTime : LocalDateTime = LocalDateTime.now(),
    var endTime: LocalDateTime? = null,
    var progressTime : Long = 0,
    var pause : Boolean = true
)
