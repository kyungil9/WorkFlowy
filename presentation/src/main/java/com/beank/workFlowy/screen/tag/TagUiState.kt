package com.beank.workFlowy.screen.tag

import androidx.compose.runtime.Stable
import com.beank.presentation.R

@Stable
data class TagUiState(
    val tagImageList : List<Int> = emptyList(),
    val title : String = "",
    val selectImage : Int = R.drawable.baseline_menu_book_24
)