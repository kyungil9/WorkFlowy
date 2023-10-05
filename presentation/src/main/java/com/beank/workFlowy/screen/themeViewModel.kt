package com.beank.workFlowy.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beank.domain.usecase.setting.GetDarkTheme
import com.beank.domain.usecase.setting.GetDynamicTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class themeViewModel @Inject constructor(
    private val getDynamicTheme: GetDynamicTheme,
    private val getDarkTheme: GetDarkTheme
) : ViewModel(){

    var darkTheme by mutableStateOf(false)
        private set

    var dynamicTheme by mutableStateOf(false)
        private set

    init {
        getDarkThemeInfo()
        getDynamicThemeInfo()
    }

    private fun getDarkThemeInfo() = getDarkTheme()
        .flowOn(Dispatchers.IO).onEach {
            darkTheme = it
        }.launchIn(viewModelScope)

    private fun getDynamicThemeInfo() = getDynamicTheme()
        .flowOn(Dispatchers.IO).onEach {
            dynamicTheme = it
        }.launchIn(viewModelScope)
}