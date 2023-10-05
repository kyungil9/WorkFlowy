package com.beank.data.mapper

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.beank.data.entity.WeekUserInfo
import com.beank.data.entity.WeekRecord
import com.beank.data.entity.WeekSchedule
import com.beank.data.entity.WeekTag
import com.beank.domain.model.Record
import com.beank.domain.model.Schedule
import com.beank.domain.model.Tag
import com.beank.domain.model.UserInfo
import java.net.URI

@RequiresApi(Build.VERSION_CODES.O)
fun WeekSchedule.toScheduleModel() = Schedule(
    id = id,
    date = date.toLocalDate(),
    startTime = startTime.toLocalTime(),
    endTime = endTime.toLocalTime(),
    time = time,
    icon = icon,
    title = title,
    comment = comment,
    check = check,
    alarm = alarm,
    alarmTime = alarmTime.toLocalDateTime(),
    alarmState = alarmState
)

@RequiresApi(Build.VERSION_CODES.O)
fun WeekRecord.toRecordModel() = Record(
    id = id,
    tag = tag,
    date = date.toLocalDate(),
    startTime = startTime.toLocalDateTime(),
    endTime = endTime?.toLocalDateTime(),
    progressTime = progressTime,
    pause = pause
)

fun WeekTag.toTagModel() = Tag(
    id = id,
    icon = icon,
    title = title
)

fun WeekUserInfo.toUserInfo() = UserInfo(
    id = id,
    nickname = nickname,
    grade = grade,
    urlImage = urlImage?.let { Uri.parse(urlImage) }
)

@RequiresApi(Build.VERSION_CODES.O)
fun Schedule.toWeekSchedule() = WeekSchedule(
    id = id,
    date = date.toInt(),
    startTime = startTime.toFormatString(),
    endTime = endTime.toFormatString(),
    time = time,
    icon = icon,
    title = title,
    comment = comment,
    check = check,
    alarm = alarm,
    alarmTime = alarmTime.toLong(),
    alarmState = alarmState
)

@RequiresApi(Build.VERSION_CODES.O)
fun Record.toWeekRecord() = WeekRecord(
    id = id,
    tag = tag,
    date = date.toInt(),
    startTime = startTime.toLong(),
    endTime = endTime?.toLong(),
    progressTime = progressTime,
    pause = pause
)

fun Tag.toWeekTag() = WeekTag(
    id = id,
    icon = icon,
    title = title
)

fun UserInfo.toWeekUserInfo() = WeekUserInfo(
    id = id,
    nickname = nickname,
    grade = grade,
    urlImage = urlImage.toString()
)

@RequiresApi(Build.VERSION_CODES.O)
fun modelCasting(model : Any) : Any = when(model){
    is String -> "oo"
    is WeekRecord -> model.toRecordModel()
    is WeekSchedule -> model.toScheduleModel()
    is WeekTag -> model.toTagModel()
    is WeekUserInfo -> model.toUserInfo()
    else -> model
}