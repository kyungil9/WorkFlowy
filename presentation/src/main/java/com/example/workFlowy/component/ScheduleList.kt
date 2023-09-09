package com.example.workFlowy.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.Schedule
import com.example.workFlowy.R
import com.example.workFlowy.screen.home.WeekUiState
import com.example.workFlowy.utils.intToImage

@Composable
fun ScheduleList(
    uiState: WeekUiState,
    onClickSchedule: (Schedule) -> Unit
){
    val scrollState = rememberLazyListState()
    LazyColumn(modifier = Modifier
        .fillMaxWidth(),
        state = scrollState) {
        items(uiState.scheduleList){ schedule ->
            ScheduleItem(schedule, onClickSchedule = onClickSchedule)
        }
    }
}

@Composable
fun ScheduleItem(
    schedule : Schedule,
    onClickSchedule : (Schedule) -> Unit
){
    Card(
        Modifier
            .fillMaxWidth()
            .clickable { onClickSchedule(schedule) }
            .height(80.dp)
            .padding(10.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp)
        ) {
            Image(
                painter = painterResource(id = intToImage(schedule.icon, LocalContext.current.resources.obtainTypedArray(R.array.scheduleList))),
                contentDescription = null,
                modifier = Modifier
                    .padding(5.dp)
                    .size(50.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .offset(x=10.dp)
                    .padding(5.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = schedule.title, fontSize = 20.sp)
                Text(
                    text = schedule.comment,
                    fontSize = 16.sp)
            }
        }
    }
}

