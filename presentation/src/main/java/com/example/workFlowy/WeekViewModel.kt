package com.example.workFlowy

import androidx.lifecycle.ViewModel
import com.example.workFlowy.utils.today
import com.example.workFlowy.utils.transDayToKorean
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class WeekViewModel @Inject constructor(

) : ViewModel() {
    private val _selectDayFlow = MutableStateFlow<LocalDate>(LocalDate.now())
    val selectDayFlow get() = _selectDayFlow.asStateFlow()
    val selectDayStringFlow get() = _selectDayFlow.asStateFlow().map { "< ${it.year%100}/${it.monthValue}/${it.dayOfMonth} ${transDayToKorean(it.dayOfWeek.value)} >" }

    fun changeSelectDay(day : LocalDate){
        _selectDayFlow.value = day
    }

}