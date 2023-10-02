package com.beank.workFlowy.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.time.temporal.WeekFields
import java.util.Locale

val zeroFormat = DecimalFormat("##00")

fun transDayToKorean(date : Int):String{
    return when(date){
        1-> "월요일"
        2-> "화요일"
        3-> "수요일"
        4-> "목요일"
        5-> "금요일"
        6-> "토요일"
        7-> "일요일"
        else ->""
    }
}

fun transDayToShortKorean(date : Int):String{
    return when(date){
        1-> "월"
        2-> "화"
        3-> "수"
        4-> "목"
        5-> "금"
        6-> "토"
        7-> "일"
        else ->""
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun today():String {
    var today = LocalDate.now()
    return "< ${today.year}/${today.monthValue}/${today.dayOfMonth} ${transDayToKorean(today.dayOfWeek.value)} >"
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.toFormatString() = "${this.year%100}/${zeroFormat.format(this.monthValue)}/${zeroFormat.format(this.dayOfMonth)} ${transDayToShortKorean(this.dayOfWeek.value)}"

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.toFormatShortString() = "${this.year%100}/${zeroFormat.format(this.monthValue)}/${zeroFormat.format(this.dayOfMonth)}"


@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.toWeekString() : String {
    val startDate = this.toWeekStart()
    val endDate = this.toWeekEnd()
    return "${startDate.year%100}/${zeroFormat.format(startDate.monthValue)}/${zeroFormat.format(startDate.dayOfMonth)}~${zeroFormat.format(endDate.monthValue)}/${zeroFormat.format(endDate.dayOfMonth)}"
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.toWeekStart(): LocalDate = this.minusDays((this.dayOfWeek.value-1).toLong())

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.toWeekEnd(): LocalDate = this.toWeekStart().plusDays(6)


@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.toMonthString() = "${this.year%100}/${zeroFormat.format(this.monthValue)}"

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.toYearString() = "${this.year}"

@RequiresApi(Build.VERSION_CODES.O)
fun makeDayList() : List<LocalDate>{
    val dayList = ArrayList<LocalDate>()
    var startDay = LocalDate.of(2021,12,28)
    val endDay = LocalDate.of(2026,1,3)
    for (i in 0 .. ChronoUnit.DAYS.between(startDay,endDay)){
        dayList.add(startDay)
        startDay = startDay.plusDays(1)
    }
    return dayList
}

@RequiresApi(Build.VERSION_CODES.O)
fun changeDayInfo(today : LocalDate) : Int {
    var endDay : Int
    when(today.monthValue){
        2 -> {
            if(today.isLeapYear)
                endDay = 29
            else
                endDay = 28
        }
        1,3,5,7,8,10,12 -> endDay = 31
        else -> endDay = 30
    }
    return endDay
}