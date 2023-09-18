package com.beank.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId


data class WeekTag(
    @DocumentId var id : String? ="",
    var icon : Int = 0,
    val title : String = ""
)
