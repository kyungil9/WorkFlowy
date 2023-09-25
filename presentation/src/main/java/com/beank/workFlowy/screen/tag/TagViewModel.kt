package com.beank.workFlowy.screen.tag

import android.content.res.TypedArray
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.beank.domain.model.Tag
import com.beank.domain.repository.LogRepository
import com.beank.domain.usecase.TagUsecases
import com.beank.domain.usecase.tag.CheckTagTitle
import com.beank.domain.usecase.tag.InsertTag
import com.beank.presentation.R
import com.beank.workFlowy.screen.WorkFlowyViewModel
import com.beank.workFlowy.utils.imageToInt
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
    private val tagUsecases: TagUsecases,
    logRepository: LogRepository
) : WorkFlowyViewModel(logRepository) {
    private val _tagUiState = MutableStateFlow(TagUiState())
    private val _selectTagText = MutableStateFlow("")
    private val _selectTagImage = MutableStateFlow(R.drawable.baseline_menu_book_24)
    private lateinit var typedTag: TypedArray


    var selectTagText by mutableStateOf("")
        private set
    var selectTagImage by mutableStateOf(R.drawable.baseline_menu_book_24)
        private set
    var tagUiState by mutableStateOf(TagUiState())
        private set


    fun initTagImages(tagList : TypedArray){
        typedTag = tagList
        launchCatching {
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

    fun insertTagInfo() {
        launchCatching {
            tagUsecases.insertTag(Tag(null, imageToInt(selectTagImage,typedTag), selectTagText))
        }
    }

    fun checkTagText() : Boolean {
        var result = false
        launchCatching {
            result = tagUsecases.checkTagTitle(selectTagText)
        }
        return result
    }

}