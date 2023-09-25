package com.beank.data.mapper

import androidx.room.TypeConverter
import com.google.gson.Gson
import java.text.DecimalFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

val timeFormat = DecimalFormat("00")

fun LocalDate.toInt() = ((this.year * 10000) + (this.monthValue * 100) + this.dayOfMonth)


fun Int.toLocalDate() = LocalDate.of(this/10000,(this%10000)/100,this%100)!!

fun LocalTime.toFormatString() = "${timeFormat.format(this.hour)}:${timeFormat.format(this.minute)}"


fun String.toLocalTime() : LocalTime {
    val format = DateTimeFormatter.ofPattern("HH:mm")
    return LocalTime.parse(this,format)
}

fun LocalDateTime.toLong() = this.atZone(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()!!


fun Long.toLocalDateTime() = LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneOffset.UTC)

