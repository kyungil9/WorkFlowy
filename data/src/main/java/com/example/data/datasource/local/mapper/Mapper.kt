package com.example.data.datasource.local.mapper

import com.example.data.datasource.local.database.entity.WeekRecord
import com.example.data.datasource.local.database.entity.WeekSchedule
import com.example.data.datasource.local.database.entity.WeekTag
import com.example.domain.model.Record
import com.example.domain.model.Schedule
import com.example.domain.model.Tag

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