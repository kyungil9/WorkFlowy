package com.beank.domain.model

import android.net.Uri

data class UserInfo(
    var id : String? = null,
    var nickname : String = "",
    var grade : Int = -1,
    var urlImage: Uri? = null
)
