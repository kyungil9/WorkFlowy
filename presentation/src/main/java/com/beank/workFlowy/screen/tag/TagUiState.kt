package com.beank.workFlowy.screen.tag

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.beank.presentation.R

@Stable
class TagUiState(
    tagImageList : List<Int> = emptyList(),
    title : String = "",
    selectImage : Int = R.drawable.baseline_menu_book_24
){
    var tagImageList by mutableStateOf(tagImageList)
    var title by mutableStateOf(title)
    var selectImage by mutableStateOf(selectImage)
}