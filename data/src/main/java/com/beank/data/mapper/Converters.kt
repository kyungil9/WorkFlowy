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

fun LocalDate.localDateToInt() = ((this.year * 10000) + (this.monthValue * 100) + this.dayOfMonth)


fun Int.intToLocalDate() =LocalDate.of(this!!/10000,(this!!%10000)/100,this!!%100)

fun LocalTime.localTimeToString() = "${timeFormat.format(this.hour)}:${timeFormat.format(this.minute)}"


fun String.stringToLocalTime() : LocalTime {
    val format = DateTimeFormatter.ofPattern("HH:mm")
    return LocalTime.parse(this,format)
}

fun LocalDateTime.localDateTimeToLong() = this.atZone(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()!!


fun Long.longToLocalDateTime() = LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneOffset.UTC)

