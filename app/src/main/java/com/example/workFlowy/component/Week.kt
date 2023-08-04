package com.example.workFlowy.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workFlowy.WeekViewModel
import com.example.workFlowy.ui.theme.lightGreen
import com.example.workFlowy.ui.theme.white
import com.example.workFlowy.utils.makeDayList
import com.example.workFlowy.utils.transDayToShortKorean
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Composable
fun WeekLazyList(
    weekViewModel: WeekViewModel
){
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = ChronoUnit.DAYS.between(LocalDate.of(2021,12,28),LocalDate.now()).toInt() -3)
    val coroutineScope = rememberCoroutineScope()
    val selectDay by weekViewModel.selectDayFlow.collectAsState()

    LazyRow(
        state = listState
    ){
        items(makeDayList()){ day ->
            dayItem(day = day, selectDay = selectDay, onItemClick = {weekViewModel.changeSelectDay(it)}, modifier = Modifier
                .size(55.dp, 70.dp)
                .background(white, shape = RoundedCornerShape(15.dp)))
        }
        coroutineScope.launch {
            listState.animateScrollToItem(ChronoUnit.DAYS.between(LocalDate.of(2021,12,28),selectDay).toInt()-3)
        }

    }

}

@Composable
fun dayItem(day : LocalDate, selectDay : LocalDate, onItemClick : (LocalDate) -> Unit, modifier: Modifier){
    val modi = if (day.isEqual(selectDay)) modifier.border(1.dp, lightGreen,shape= RoundedCornerShape(15.dp)) else modifier
    Box(
        modifier = modi.clickable { onItemClick(day) }
    ) {
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = transDayToShortKorean(day.dayOfWeek.value),
                textAlign = TextAlign.Center,
                fontSize = 12.sp)
            Text(
                text = day.dayOfMonth.toString(),
                textAlign = TextAlign.Center,
                fontSize = 24.sp)
        }
    }
}
