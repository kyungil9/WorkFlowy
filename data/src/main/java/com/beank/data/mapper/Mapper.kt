package com.beank.data.mapper

import com.beank.data.entity.WeekRecord
import com.beank.data.entity.WeekSchedule
import com.beank.data.entity.WeekTag
import com.beank.domain.model.Record
import com.beank.domain.model.Schedule
import com.beank.domain.model.Tag

fun WeekSchedule.toScheduleModel() = Schedule(
    id = id,
    date = date.toLocalDate(),
    startTime = startTime.toLocalTime(),
    endTime = endTime.toLocalTime(),
    time = time,
    icon = icon,
    title = title,
    comment = comment,
    check = check
)

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

fun Schedule.toWeekSchedule() = WeekSchedule(
    id = id,
    date = date.toInt(),
    startTime = startTime.toFormatString(),
    endTime = endTime.toFormatString(),
    time = time,
    icon = icon,
    title = title,
    comment = comment,
    check = check
)

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

fun modelCasting(model : Any) : Any = when(model){
    is String -> "oo"
    is WeekRecord -> model.toRecordModel()
    is WeekSchedule -> model.toScheduleModel()
    is WeekTag -> model.toTagModel()
    else -> model
}