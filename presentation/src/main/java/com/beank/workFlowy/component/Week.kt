package com.beank.workFlowy.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beank.workFlowy.ui.theme.lightGreen
import com.beank.workFlowy.ui.theme.shapes
import com.beank.workFlowy.ui.theme.white
import com.beank.workFlowy.utils.makeDayList
import com.beank.workFlowy.utils.transDayToShortKorean
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
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
            dayItem(day = day, selectDay = selectDay, onItemClick = onClickItem)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun dayItem(day : LocalDate, selectDay : LocalDate, onItemClick : (LocalDate) -> Unit){
    Card(
        onClick = { onItemClick(day)},
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
        modifier = Modifier
            .size(55.dp, 70.dp)
            .border(1.dp, color = if (day.isEqual(selectDay)) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.background, shape = MaterialTheme.shapes.small)
    ) {
        val color = if (day.isEqual(LocalDate.now())) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.background
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = transDayToShortKorean(day.dayOfWeek.value),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge)
            Text(
                text = day.dayOfMonth.toString(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.drawBehind {
                    drawCircle(color = color, radius = this.size.maxDimension/ 2f)
                }
            )
        }
    }
}
