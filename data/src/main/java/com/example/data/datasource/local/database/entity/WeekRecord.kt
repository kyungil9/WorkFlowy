package com.example.data.datasource.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class WeekRecord(
    @PrimaryKey(autoGenerate = true) val id : Int,
    val tag : String,
    var startTime : LocalDateTime,
    var endTime: LocalDateTime?,
    var progressTime : Long,
    var pause : Boolean
)
