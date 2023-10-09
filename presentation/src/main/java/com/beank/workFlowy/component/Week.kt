package com.beank.workFlowy.component

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.beank.workFlowy.utils.Date
import com.beank.workFlowy.utils.toInt
import com.beank.workFlowy.utils.transDayToShortKorean
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeekLazyList(
    weekListState : LazyListState,
    onClickItem : (LocalDate) -> Unit,
    weekDayList : () -> List<Date>
){
    Log.d("recomposition","weeklist")
    LazyRow(
        state = weekListState,
        modifier = Modifier.fillMaxWidth().height(70.dp)
    ){
        items(weekDayList(), key = {it.hashCode()}){ day ->
            dayItem(day = {day}, onItemClick = onClickItem)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun dayItem(day : () ->Date, onItemClick : (LocalDate) -> Unit){
    Log.d("recomposition","weekitem")
    Card(
        onClick = remember{{ onItemClick(day().date) }},
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
        modifier = Modifier
            .size(55.dp, 70.dp)
            .border(
                1.dp,
                color = if (day().isChecked) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.background,
                shape = MaterialTheme.shapes.small
            )
    ) {
        val color = if (day().date.isEqual(LocalDate.now())) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.background
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = transDayToShortKorean(day().date.dayOfWeek.value),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge)
            Text(
                text = day().date.dayOfMonth.toString(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.drawBehind {
                    drawCircle(color = color, radius = this.size.maxDimension/ 2f)
                }
            )
        }
    }
}
