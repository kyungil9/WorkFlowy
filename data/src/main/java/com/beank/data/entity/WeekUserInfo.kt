package com.beank.data.entity

import com.google.firebase.firestore.DocumentId
import java.net.URL

data class WeekUserInfo(
    @DocumentId var id : String? = null,
    var nickname : String = "",
    var grade : Int = 0,
    var urlImage: String? = null
)