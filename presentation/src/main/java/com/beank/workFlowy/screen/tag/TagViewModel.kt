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
import com.beank.presentation.R
import com.beank.workFlowy.screen.WorkFlowyViewModel
import com.beank.workFlowy.utils.imageToInt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TagViewModel @Inject constructor(
    private val tagUsecases: TagUsecases,
    logRepository: LogRepository
) : WorkFlowyViewModel(logRepository) {

    private lateinit var typedTag: TypedArray
    var uiState by mutableStateOf(TagUiState())
        private set

    private val title get() = uiState.title
    private val selectImage get() = uiState.selectImage

    fun initTagImages(tagList : TypedArray){
        typedTag = tagList
        launchCatching {
            val tagImages = ArrayList<Int>()
            for (i in 0 until tagList.length()-2){
                tagImages.add(tagList.getResourceId(i,-1))
            }
            viewModelScope.launch(Dispatchers.Main) {
                uiState = uiState.copy(tagImageList = tagImages)
            }
        }
    }

    fun updateSelectTagText(title : String){
        uiState = uiState.copy(title = title)
    }

    fun updateSelectTagImage(image : Int){
        uiState = uiState.copy(selectImage = image)
    }

    fun insertTagInfo() {
        launchCatching {
            tagUsecases.insertTag(Tag(null, imageToInt(selectImage,typedTag), title))
        }
    }

    fun checkTagText() : Boolean {
        var result = false
        launchCatching {
            result = tagUsecases.checkTagTitle(title)
        }
        return result
    }

}