package com.example.workFlowy.screen.tag

import android.content.res.TypedArray
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Tag
import com.example.domain.usecase.TagUsecase
import com.example.workFlowy.R
import com.example.workFlowy.utils.imageToInt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
data class TagUiState(
    val tagImageList : List<Int> = emptyList()
)

@HiltViewModel
class TagViewModel @Inject constructor(
    private val tagUsecase : TagUsecase
) : ViewModel() {
    private val _tagUiState = MutableStateFlow(TagUiState())
    private val _selectTagText = MutableStateFlow("")
    private val _selectTagImage = MutableStateFlow(R.drawable.baseline_menu_book_24)
    private lateinit var typedTag: TypedArray


    val selectTagText get() = _selectTagText.asStateFlow()
    val selectTagImage get() = _selectTagImage.asStateFlow()
    val tagUiState get() = _tagUiState.asStateFlow()


    fun initTagImages(tagList : TypedArray){
        typedTag = tagList
        viewModelScope.launch(Dispatchers.IO) {
            val tagImages = ArrayList<Int>()
            for (i in 0 until tagList.length()-2){
                tagImages.add(tagList.getResourceId(i,-1))
            }
            _tagUiState.update { state -> state.copy(tagImageList = tagImages) }
        }
    }

    fun updateSelectTagText(text : String){
        _selectTagText.value = text
    }

    fun updateSelectTagImage(image : Int){
        _selectTagImage.value = image
    }

    fun insertTag() {
        viewModelScope.launch(Dispatchers.IO) {
            tagUsecase.insertTag(Tag(null, imageToInt(selectTagImage.value,typedTag), selectTagText.value))
        }
    }

    fun checkTagText() = tagUsecase.checkTagTitle(selectTagText.value)

}