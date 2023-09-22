package com.beank.workFlowy.utils

import com.beank.domain.model.Schedule
import com.google.gson.Gson
import java.text.DecimalFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun Schedule.toScheduleJson() : String?{
    val temp = JsonSchedule(
        this.id,
        this.date.toInt(),
        this.startTime.toFormatString(),
        this.endTime.toFormatString(),
        this.icon,
        this.title,
        this.comment
    )
    return Gson().toJson(temp)
}

fun String.fromScheduleJson(): Schedule{
    val json = Gson().fromJson(this,JsonSchedule::class.java)
    return Schedule(
        json.id,
        json.date.toLocalDate(),
        json.startTime.toLocalTime(),
        json.endTime.toLocalTime(),
        json.icon,
        json.title,
        json.comment
    )
}

data class JsonSchedule(
    var id: String? = null,
    var date: Int = 0,
    var startTime: String = "06:00",
    var endTime: String = "06:00",
    var icon: Int = 0,
    var title: String = "",
    var comment: String = ""
)


val timeFormat = DecimalFormat("00")

fun LocalDate.toInt() = ((this.year * 10000) + (this.monthValue * 100) + this.dayOfMonth)


fun Int.toLocalDate() = LocalDate.of(this/10000,(this%10000)/100,this%100)



fun LocalTime.toFormatString() = "${timeFormat.format(this.hour)}:${timeFormat.format(this.minute)}"


fun String.toLocalTime() : LocalTime {
    val format = DateTimeFormatter.ofPattern("HH:mm")
    return LocalTime.parse(this,format)
}

fun LocalDate.toStartTimeLong() = LocalDateTime.of(this, LocalTime.of(0,0)).toLong()

fun LocalDate.toEndTimeLong() = LocalDateTime.of(this, LocalTime.of(23,59)).toLong()

fun LocalDateTime.toLong() = this.atZone(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()!!

fun Long.toLocalDateTime() = LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneOffset.UTC)

