package com.beank.workFlowy.component

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.beank.domain.model.Schedule
import com.beank.presentation.R
import com.beank.workFlowy.utils.intToImage
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.material3.IconButton
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import com.beank.workFlowy.utils.toFormatString
import kotlin.math.abs

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleList(
    scheduleList : () -> List<Schedule>,
    onRightDrag : () -> Unit,
    onLeftDrag : () -> Unit,
    onClickSchedule: (Schedule) -> Unit
){
    val scrollState = rememberLazyListState()
    var dragOffsetX by remember { mutableFloatStateOf(0f) }
    var direction by remember { mutableIntStateOf(-1) }
    Log.d("recomposition","schedulelist")
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
        items(scheduleList(),key = {item -> item.id!! }) { schedule ->
            ScheduleItem({schedule}, onClickSchedule = onClickSchedule)
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleItem(
    schedule : () -> Schedule,
    onClickSchedule : (Schedule) -> Unit
){
    var commentToggle by remember { mutableStateOf(false)}
    val cardColor by animateColorAsState(targetValue = if (schedule().check) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.primaryContainer, tween(500),
        label = ""
    )
    Card(
        Modifier
            .fillMaxWidth()
            .clickable (onClick = remember{{onClickSchedule(schedule()) }})
            .height(if (schedule().time && commentToggle) 160.dp else (if (commentToggle) 130.dp else (if (schedule().time) 100.dp else 80.dp)))
            .padding(10.dp),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 5.dp, top = 0.dp, end = 0.dp, bottom = 0.dp)
                .background(
                    color = cardColor,
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 5.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 5.dp
                    )
                )
                .padding(horizontal = 5.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row() {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = intToImage(schedule().icon, LocalContext.current.resources.obtainTypedArray(R.array.scheduleList))),
                        contentDescription = "스케줄 이미지",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = schedule().title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(start = 15.dp).width(200.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                IconButton(onClick = { commentToggle = commentToggle.not()}, modifier = Modifier.padding(end = 5.dp)) {
                    Icon(
                        imageVector = if (commentToggle) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                        contentDescription = "아이콘 선택",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp)
            ) {
                if (schedule().time){
                    Text(text = "${schedule().startTime.toFormatString()}~${schedule().endTime.toFormatString()}", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.outline)
                }
                if (schedule().alarm){
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.baseline_notifications_none_24),
                        contentDescription = "알람 이미지",
                        modifier = Modifier
                            .padding(start = if (schedule().time) 15.dp else 0.dp, bottom = 10.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                }
            }
            if (commentToggle){
                Card(
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth()
                        .padding(vertical = 5.dp, horizontal = 20.dp),
                    shape = MaterialTheme.shapes.small,
                    colors = CardDefaults.cardColors(cardColor),
                    border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.onSecondaryContainer)
                ) {
                    Text(
                        text = schedule().comment, style = MaterialTheme.typography.bodyLarge,
                        maxLines = 2, color = MaterialTheme.colorScheme.onPrimaryContainer,
                        overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(start = 10.dp)
                    )
                }

            }
        }
    }
}

