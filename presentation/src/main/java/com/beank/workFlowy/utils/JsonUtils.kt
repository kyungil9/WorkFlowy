package com.beank.workFlowy.utils

import com.beank.domain.model.Schedule
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun Schedule.toScheduleJson() : String?{
    val temp = JsonSchedule(
        this.id,
        this.date.localDateToInt(),
        this.startTime.localTimeToString(),
        this.endTime.localTimeToString(),
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
        json.date.intToLocalDate(),
        json.startTime.stringToLocalTime(),
        json.endTime.stringToLocalTime(),
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

fun LocalDate.localDateToInt() = ((this.year * 10000) + (this.monthValue * 100) + this.dayOfMonth)


fun Int.intToLocalDate() = LocalDate.of(this!!/10000,(this!!%10000)/100,this!!%100)



fun LocalTime.localTimeToString() = "${timeFormat.format(this.hour)}:${timeFormat.format(this.minute)}"


fun String.stringToLocalTime() : LocalTime {
    val format = DateTimeFormatter.ofPattern("HH:mm")
    return LocalTime.parse(this,format)
}
