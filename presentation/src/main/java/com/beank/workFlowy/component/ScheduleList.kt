package com.beank.workFlowy.component

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beank.domain.model.Schedule
import com.beank.presentation.R
import com.beank.workFlowy.screen.home.WeekUiState
import com.beank.workFlowy.utils.intToImage
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import java.time.LocalDate
import kotlin.math.abs

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScheduleList(
    uiState: WeekUiState,
    onRightDrag : () -> Unit,
    onLeftDrag : () -> Unit,
    onClickSchedule: (Schedule) -> Unit
){
    val scrollState = rememberLazyListState()
    var dragOffsetX by remember { mutableFloatStateOf(0f) }
    var direction by remember { mutableIntStateOf(-1) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        val (x, y) = dragAmount
                        if (abs(x) > abs(y)) {
                            when {
                                x > 0 -> {
                                    dragOffsetX += x
                                    direction = 0
                                }

                                x < 0 -> {
                                    dragOffsetX += x
                                    direction = 1
                                }
                            }
                        }
                    },
                    onDragEnd = {
                        when (direction) {
                            0 -> {
                                if (dragOffsetX > 400) {
                                    //left motion
                                    onLeftDrag()
                                }
                                dragOffsetX = 0f
                            }

                            1 -> {
                                if (dragOffsetX < -400) {
                                    //right
                                    onRightDrag()

                                }
                                dragOffsetX = 0f
                            }
                        }
                    }
                )
            },
        state = scrollState
    ) {
        items(uiState.scheduleList) { schedule ->
            ScheduleItem(schedule, onClickSchedule = onClickSchedule)
        }
    }

}

@Composable
fun ScheduleItem(
    schedule : Schedule,
    onClickSchedule : (Schedule) -> Unit
){
    val cardColor by animateColorAsState(targetValue = if (schedule.check) Color.Gray else Color.White, tween(500))
    Card(
        Modifier
            .fillMaxWidth()
            .clickable { onClickSchedule(schedule) }
            .height(80.dp)
            .padding(10.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
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
                    .offset(x = 10.dp)
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

