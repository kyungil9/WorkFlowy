package com.beank.workFlowy.screen.tag

import android.content.res.TypedArray
import com.beank.domain.model.Tag
import com.beank.domain.repository.LogRepository
import com.beank.domain.usecase.TagUsecases
import com.beank.workFlowy.screen.WorkFlowyViewModel
import com.beank.workFlowy.utils.imageToInt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TagViewModel @Inject constructor(
    private val tagUsecases: TagUsecases,
    logRepository: LogRepository
) : WorkFlowyViewModel(logRepository) {

    private lateinit var typedTag: TypedArray
    val uiState = TagUiState()

    fun initTagImages(tagList : TypedArray){
        typedTag = tagList
        ioScope.launch {
            val tagImages = ArrayList<Int>()
            for (i in 0 until tagList.length()-2){
                tagImages.add(tagList.getResourceId(i,-1))
            }
            withContext(Dispatchers.Main.immediate) {
                uiState.tagImageList = tagImages
            }
        }
    }

    fun updateSelectTagText(title : String){
        uiState.title = title
    }

    fun updateSelectTagImage(image : Int){
        uiState.selectImage = image
    }

    fun insertTagInfo() {
        ioScope.launch {
            tagUsecases.insertTag(Tag(null, imageToInt(uiState.selectImage,typedTag), uiState.title))
        }
    }

    fun checkTagText() : Boolean {
        var result = false
        ioScope.launch {
            result = tagUsecases.checkTagTitle(uiState.title)
        }
        return result
    }

}