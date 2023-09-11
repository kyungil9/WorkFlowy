package com.beank.data.datasource.local.mapper

import com.beank.data.datasource.local.database.entity.WeekRecord
import com.beank.data.datasource.local.database.entity.WeekSchedule
import com.beank.data.datasource.local.database.entity.WeekTag
import com.beank.domain.model.Record
import com.beank.domain.model.Schedule
import com.beank.domain.model.Tag

fun WeekSchedule.toScheduleModel() = Schedule(
    id = id,
    date = date,
    startTime = startTime,
    endTime = endTime,
    icon = icon,
    title = title,
    comment = comment
)

fun WeekRecord.toRecordModel() = Record(
    id = id,
    tag = tag,
    startTime = startTime,
    endTime = endTime,
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
    date = date,
    startTime = startTime,
    endTime = endTime,
    icon = icon,
    title = title,
    comment = comment
)

fun Record.toWeekRecord() = WeekRecord(
    id = id,
    tag = tag,
    startTime = startTime,
    endTime = endTime,
    progressTime = progressTime,
    pause = pause
)

fun Tag.toWeekTag() = WeekTag(
    id = id,
    icon = icon,
    title = title
)