package com.example.data.datasource.local.database.entity

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WeekTag(
    @PrimaryKey(autoGenerate = true) val id : Int? = 0,
    var icon : Int,
    val title : String
)
