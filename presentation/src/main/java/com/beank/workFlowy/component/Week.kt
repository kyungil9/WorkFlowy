package com.beank.workFlowy.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beank.workFlowy.ui.theme.lightGreen
import com.beank.workFlowy.ui.theme.white
import com.beank.workFlowy.utils.makeDayList
import com.beank.workFlowy.utils.transDayToShortKorean
import java.time.LocalDate

@Composable
fun WeekLazyList(
    selectDay: LocalDate,
    weekListState : LazyListState,
    onClickItem : (LocalDate) -> Unit
){
    LazyRow(
        state = weekListState
    ){
        items(makeDayList()){ day ->
            dayItem(day = day, selectDay = selectDay, onItemClick = onClickItem, modifier = Modifier
                .size(55.dp, 70.dp)
                .background(white, shape = RoundedCornerShape(15.dp)))
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
