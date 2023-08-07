package com.example.workFlowy.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.Schedule
import com.example.workFlowy.R
import com.example.workFlowy.WeekUiState
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Composable
fun ScheduleList(
    uiState: WeekUiState
){
    val scrollState = rememberLazyListState()
    LazyColumn(modifier = Modifier
        .fillMaxWidth(),
        state = scrollState) {
        items(uiState.scheduleList){ schedule ->
            ScheduleItem(schedule)
        }
    }
}

@Composable
fun ScheduleItem(schedule : Schedule){
    Card(
        Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(10.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = 5.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp)
        ) {
            Image(
                painter = painterResource(id = schedule.icon),
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

