package com.example.domain.model

import androidx.annotation.DrawableRes

data class Tag(
    var id : Int?,
    @DrawableRes var icon : Int,
    val title : String
)
