package com.beank.workFlowy.component

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.beank.presentation.R
import com.beank.workFlowy.navigation.NavigationItem

@Composable
fun WeekBottomBar(
    checked : () -> Boolean,
    onMoveMisson : () -> Unit,
    onMoveToday : () -> Unit,
    onCheckSchedule : () -> Unit,
    onDeleteSchedule : () -> Unit,
    onUpdateSchedule : () -> Unit,
    onAdditionalSchedule : () -> Unit
){
    Log.d("recomposition","bottombar")
    BottomAppBar(
        actions = remember{{
            IconButton(onClick = onMoveMisson) {
                Icon(
                    modifier = Modifier
                        .size(30.dp)
                        .clickable(
                            onClick = onMoveMisson,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = false, radius = 20.dp)
                        ),
                    imageVector = ImageVector.vectorResource(id = NavigationItem.MISSON.icon!!),
                    contentDescription = "미션 이동")
            }
            AnimatedVisibility(visible = checked().not()) {
                IconButton(onClick = onMoveToday) {
                    Icon(
                        modifier = Modifier
                            .size(30.dp)
                            .clickable(
                                onClick = onMoveToday,
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(bounded = false, radius = 20.dp)
                            ),
                        imageVector = ImageVector.vectorResource(id = R.drawable.baseline_today_24),
                        contentDescription = "오늘날짜 이동")
                }
            }
            AnimatedVisibility(visible = checked()) {
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
                            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_check_box_24),
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
                            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_delete_outline_24),
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
                            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_edit_24),
                            contentDescription = "스케줄 수정")
                    }
                }
            }
        }},
        containerColor = MaterialTheme.colorScheme.inversePrimary,
        floatingActionButton = remember{{
            FloatingActionButton(
                onClick = onAdditionalSchedule,
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            ) {
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.baseline_add_24), contentDescription = "스케줄 추가", tint = MaterialTheme.colorScheme.onTertiaryContainer)
            }
        }}
    )
}