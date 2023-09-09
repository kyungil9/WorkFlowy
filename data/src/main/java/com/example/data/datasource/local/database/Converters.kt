package com.example.data.datasource.local.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class Converters {
    @TypeConverter
    fun localDateToJson(value: LocalDate?) : Int?{
        return ((value?.year!! * 10000) + (value?.monthValue!! * 100) + value?.dayOfMonth!!)
    }

    @TypeConverter
    fun jsonToLocalDate(value: Int?) : LocalDate? {
        return LocalDate.of(value!!/10000,(value!!%10000)/100,value!!%100)
    }


    @TypeConverter
    fun localTimeToJson(value: LocalTime) : String?{
        return Gson().toJson(value.toString())
    }

    @TypeConverter
    fun jsonToLocalTime(value: String) : LocalTime?{
        val string = Gson().fromJson(value,String::class.java)
        val format = DateTimeFormatter.ofPattern("HH:mm")
        return LocalTime.parse(string,format)
    }

    @TypeConverter
    fun localDateTimeToJson(value: LocalDateTime?) : Long?{
        return value?.atZone(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()
    }

    @TypeConverter
    fun jsonToLocalDateTime(value: Long?) : LocalDateTime? {
        return value?.let { LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneOffset.UTC)}
    }
}