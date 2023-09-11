package com.example.workFlowy.utils

import java.text.DecimalFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Calendar

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

fun today():String {
    var today = LocalDate.now()
    return "< ${today.year}/${today.monthValue}/${today.dayOfMonth} ${transDayToKorean(today.dayOfWeek.value)} >"
}

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