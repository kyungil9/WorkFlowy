package com.example.data.datasource.local.database.entity

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Entity
data class WeekSchedule(
    @PrimaryKey(autoGenerate = true) val id : Int,
    var date : LocalDate,
    var startTime : LocalTime,
    var endTime : LocalTime,
    @DrawableRes var icon : Int,
    var title : String,
    var comment : String
)
