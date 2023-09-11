package com.beank.data.datasource.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity
data class WeekSchedule(
    @PrimaryKey(autoGenerate = true) val id : Int? = 0,
    var date : LocalDate,
    var startTime : LocalTime,
    var endTime : LocalTime,
    var icon : Int,
    var title : String,
    var comment : String
)
