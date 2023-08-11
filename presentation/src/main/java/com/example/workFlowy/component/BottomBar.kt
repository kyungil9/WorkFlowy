package com.example.workFlowy.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.workFlowy.R
import com.example.workFlowy.navigation.NavigationItem

@Composable
fun WeekBottomBar(
    checked : Boolean,
    onMoveMisson : () -> Unit,
    onCheckSchedule : () -> Unit,
    onDeleteSchedule : () -> Unit,
    onUpdateSchedule : () -> Unit,
    onAdditionalSchedule : () -> Unit
){
    BottomAppBar(
        actions = {
            IconButton(onClick = onMoveMisson) {
                Icon(
                    modifier = Modifier
                        .size(30.dp)
                        .clickable(
                            onClick = onMoveMisson,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = false, radius = 20.dp)
                        ),
                    painter = painterResource(id = NavigationItem.MISSON.icon!!),
                    contentDescription = "미션 이동")
            }
            AnimatedVisibility(visible = checked) {
                Row {
                    IconButton(onClick = onCheckSchedule) {
                        Icon(
                            modifier = Modifier
                                .size(30.dp)
                                .clickable(
                                    onClick = onCheckSchedule,
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = rememberRipple(bounded = false, radius = 20.dp)
                                ),
                            painter = painterResource(id = R.drawable.baseline_check_box_24),
                            contentDescription = "스케줄 완료")
                    }
                    IconButton(onClick = onDeleteSchedule) {
                        Icon(
                            modifier = Modifier
                                .size(30.dp)
                                .clickable(
                                    onClick = onDeleteSchedule,
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = rememberRipple(bounded = false, radius = 20.dp)
                                ),
                            painter = painterResource(id = R.drawable.baseline_delete_outline_24),
                            contentDescription = "스케줄 삭제")
                    }
                    IconButton(onClick = onUpdateSchedule) {
                        Icon(
                            modifier = Modifier
                                .size(30.dp)
                                .clickable(
                                    onClick = onUpdateSchedule,
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = rememberRipple(bounded = false, radius = 20.dp)
                                ),
                            painter = painterResource(id = R.drawable.baseline_edit_24),
                            contentDescription = "스케줄 수정")
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAdditionalSchedule) {
                Icon(painter = painterResource(id = R.drawable.baseline_add_24), contentDescription = "스케줄 추가")
            }
        }
    )
}