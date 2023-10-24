package com.beank.workFlowy.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.beank.domain.model.GeofenceData
import com.beank.domain.model.GeofenceEvent
import com.beank.domain.model.Schedule
import com.google.gson.Gson
import java.text.DecimalFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
fun Schedule.toScheduleJson() : String?{
    val temp = JsonSchedule(
        id,
        date.toInt(),
        startTime.toFormatString(),
        endTime.toFormatString(),
        time,
        icon,
        title,
        comment,
        check,
        alarm,
        alarmState
    )
    return Gson().toJson(temp)
}

@RequiresApi(Build.VERSION_CODES.O)
fun GeofenceData.toGeofenceJson() : String? {
    val temp = JsonGeofenceData(
        id,
        enterTag,
        enterTagImage,
        exitTag,
        exitTagImage,
        latitude,
        lonitude,
        radius,
        delayTime,
        timeOption,
        startTime.toFormatString(),
        endTime.toFormatString(),
        geoEvent
    )
    return Gson().toJson(temp)
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.fromScheduleJson(): Schedule{
    val json = Gson().fromJson(this,JsonSchedule::class.java)
    return Schedule(
        json.id,
        json.date.toLocalDate(),
        json.startTime.toLocalTime(),
        json.endTime.toLocalTime(),
        json.time,
        json.icon,
        json.title,
        json.comment,
        json.check,
        json.alarm,
        alarmState = json.alarmState
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.fromGeofenceJson() : GeofenceData{
    val json = Gson().fromJson(this,JsonGeofenceData::class.java)
    return GeofenceData(
        json.id,
        json.enterTag,
        json.enterTagImage,
        json.exitTag,
        json.exitTagImage,
        json.latitude,
        json.lonitude,
        json.radius,
        json.delayTime,
        json.timeOption,
        json.startTime.toLocalTime(),
        json.endTime.toLocalTime(),
        json.geoEvent
    )
}



data class JsonSchedule(
    var id: String? = null,
    var date: Int = 0,
    var startTime: String = "06:00",
    var endTime: String = "06:00",
    var time : Boolean = false,
    var icon: Int = 0,
    var title: String = "",
    var comment: String = "",
    var check : Boolean = false,
    var alarm : Boolean = false,
    var alarmState : String = "5분전"
)

data class JsonGeofenceData(
    var id : String? = null,
    var enterTag : String = "",
    var enterTagImage : Int = 0,
    var exitTag : String = "",
    var exitTagImage : Int = 0,
    var latitude : Double = 0.0,
    var lonitude : Double = 0.0,
    var radius : Float = 100f,
    var delayTime : Int = 300000,
    var timeOption : Boolean = false,
    var startTime : String = "00:00",
    var endTime : String = "00:00",
    var geoEvent : Int = GeofenceEvent.EnterRequest
)


val timeFormat = DecimalFormat("00")

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.toInt() = ((this.year * 10000) + (this.monthValue * 100) + this.dayOfMonth)


@RequiresApi(Build.VERSION_CODES.O)
fun Int.toLocalDate() = LocalDate.of(this/10000,(this%10000)/100,this%100)


@RequiresApi(Build.VERSION_CODES.O)
fun LocalTime.toFormatString() = "${timeFormat.format(this.hour)}:${timeFormat.format(this.minute)}"

@RequiresApi(Build.VERSION_CODES.O)
fun String.toLocalTime() : LocalTime {
    val format = DateTimeFormatter.ofPattern("HH:mm")
    return LocalTime.parse(this,format)
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.toStartTimeLong() = LocalDateTime.of(this, LocalTime.of(0,0)).toLong()

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.toEndTimeLong() = LocalDateTime.of(this, LocalTime.of(23,59)).toLong()

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.toLong() = this.atZone(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()!!

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.toTimeMillis() : Long{
    val calendar = Calendar.getInstance()
    calendar.set(this.year,this.monthValue-1,this.dayOfMonth,this.hour,this.minute)
    return calendar.timeInMillis
}

@RequiresApi(Build.VERSION_CODES.O)
fun Long.toLocalDateTime() = LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneOffset.UTC)


