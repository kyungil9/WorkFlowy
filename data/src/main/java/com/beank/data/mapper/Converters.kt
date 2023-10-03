package com.beank.data.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.DecimalFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

val timeFormat = DecimalFormat("00")

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.toInt() = ((this.year * 10000) + (this.monthValue * 100) + this.dayOfMonth)


@RequiresApi(Build.VERSION_CODES.O)
fun Int.toLocalDate() = LocalDate.of(this/10000,(this%10000)/100,this%100)!!
@RequiresApi(Build.VERSION_CODES.O)
fun LocalTime.toFormatString() = "${timeFormat.format(this.hour)}:${timeFormat.format(this.minute)}"

@RequiresApi(Build.VERSION_CODES.O)
fun String.toLocalTime() : LocalTime {
    val format = DateTimeFormatter.ofPattern("HH:mm")
    return LocalTime.parse(this,format)
}
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.toLong() = this.atZone(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()!!

@RequiresApi(Build.VERSION_CODES.O)
fun Long.toLocalDateTime() = LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneOffset.UTC)

