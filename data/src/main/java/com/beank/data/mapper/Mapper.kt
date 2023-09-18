package com.beank.data.mapper

import com.beank.data.entity.WeekRecord
import com.beank.data.entity.WeekSchedule
import com.beank.data.entity.WeekTag
import com.beank.domain.model.Record
import com.beank.domain.model.Schedule
import com.beank.domain.model.Tag

fun WeekSchedule.toScheduleModel() = Schedule(
    id = id,
    date = date.intToLocalDate(),
    startTime = startTime.stringToLocalTime(),
    endTime = endTime.stringToLocalTime(),
    icon = icon,
    title = title,
    comment = comment
)

fun WeekRecord.toRecordModel() = Record(
    id = id,
    tag = tag,
    startTime = startTime.longToLocalDateTime(),
    endTime = endTime?.longToLocalDateTime(),
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
    date = date.localDateToInt(),
    startTime = startTime.localTimeToString(),
    endTime = endTime.localTimeToString(),
    icon = icon,
    title = title,
    comment = comment
)

fun Record.toWeekRecord() = WeekRecord(
    id = id,
    tag = tag,
    startTime = startTime.localDateTimeToLong(),
    endTime = endTime?.localDateTimeToLong(),
    progressTime = progressTime,
    pause = pause
)

fun Tag.toWeekTag() = WeekTag(
    id = id,
    icon = icon,
    title = title
)