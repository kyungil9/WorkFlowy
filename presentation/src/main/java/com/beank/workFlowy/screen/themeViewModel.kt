package com.beank.workFlowy.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.beank.domain.repository.LogRepository
import com.beank.domain.usecase.setting.GetDarkTheme
import com.beank.domain.usecase.setting.GetDynamicTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class themeViewModel @Inject constructor(
    private val getDynamicTheme: GetDynamicTheme,
    private val getDarkTheme: GetDarkTheme,
    logRepository: LogRepository
) : WorkFlowyViewModel(logRepository){

    var darkTheme by mutableStateOf(false)
        private set

    var dynamicTheme by mutableStateOf(false)
        private set

    init {
        getDarkThemeInfo()
        getDynamicThemeInfo()
    }

    private fun getDarkThemeInfo() = getDarkTheme()
        .flowOn(ioContext).onEach {
            darkTheme = it
        }.launchIn(mainScope)

    private fun getDynamicThemeInfo() = getDynamicTheme()
        .flowOn(ioContext).onEach {
            dynamicTheme = it
        }.launchIn(mainScope)
}