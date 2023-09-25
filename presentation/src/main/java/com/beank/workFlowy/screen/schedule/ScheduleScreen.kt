package com.beank.workFlowy.screen.schedule

import android.content.res.Resources
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.beank.presentation.R
import com.beank.workFlowy.component.DaysPicker
import com.beank.workFlowy.component.HorizontalSpacer
import com.beank.workFlowy.component.VerticalSpacer
import com.beank.workFlowy.component.WeekAppBar
import com.beank.workFlowy.component.WeekLayout
import com.beank.workFlowy.component.snackbar.SnackbarManager
import com.beank.workFlowy.ui.theme.black
import com.beank.workFlowy.ui.theme.gray
import com.beank.workFlowy.ui.theme.white


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    scheduleViewModel: ScheduleViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    resource : Resources,
    onBackHome : () -> Unit
){

    scheduleViewModel.initScheduleImages(resource.obtainTypedArray(R.array.scheduleList))
    val uiState = scheduleViewModel.scheduleUiState
    val selectYear = scheduleViewModel.selectPickerYear
    val selectMonth = scheduleViewModel.selectPickerMonth
    val selectDay = scheduleViewModel.selectPickerDay
    val selectStartTime = scheduleViewModel.selectPickerStartTime
    val selectEndTime = scheduleViewModel.selectPickerEndTime
    var checkTime = scheduleViewModel.checkTime
    var checkendTime = scheduleViewModel.checkendTime

    WeekLayout(
        snackbarHostState = snackbarHostState,
        topBar = {
            WeekAppBar(
                headerIcon = R.drawable.baseline_close_24,
                onHeaderIconClick = { onBackHome() },
                selectDay = "일정",
                tailIcon = R.drawable.baseline_check_24,
                onTailIconClick = {
                    if (scheduleViewModel.updateId.isNotEmpty())
                        scheduleViewModel.updateScheduleInfo()
                    else
                        scheduleViewModel.insertScheduleInfo()
                    onBackHome()
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp)
            ) {
                TextField(
                    value = scheduleViewModel.inputScheduleText,
                    onValueChange = {scheduleViewModel.updateScheduleText(it)},
                    label = { Text(text = "일정")}
                )
                Image(
                    painter = painterResource(id = scheduleViewModel.selectScheduleImage),
                    contentDescription = "스캐줄사진",
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            scheduleViewModel.updateCheckImage()
                        }
                )
            }
            AnimatedVisibility(visible = scheduleViewModel.checkImage) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(40.dp),
                    verticalArrangement = Arrangement.spacedBy(space = 5.dp),
                    horizontalArrangement = Arrangement.spacedBy(space = 5.dp),
                    contentPadding = PaddingValues(all = 5.dp)
                ){
                    items(uiState.scheduleImageList){image ->
                        Image(
                            painter = painterResource(id = image),
                            contentDescription = "스캐줄사진리스트",
                            modifier = Modifier
                                .height(40.dp)
                                .clickable {
                                    scheduleViewModel.updateScheduleImage(image)
                                }
                        )
                    }
                }
            }
            Divider(
                color = gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(start = 10.dp, end = 10.dp)
            ){
                Row {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            scheduleViewModel.updateCheckDate()
                            scheduleViewModel.updateCheckEndTime(false)
                        }
                    ) {
                        Text(
                            text = "$selectYear.$selectMonth.$selectDay(${scheduleViewModel.selectDayOfWeek(selectYear,selectMonth,selectDay)})"
                        )
                        if (checkTime){
                            Text(
                                text ="${selectStartTime.hours}:${selectStartTime.hours}"
                            )
                        }
                    }
                    HorizontalSpacer(width = 20.dp)
                    AnimatedVisibility(visible = checkTime) {
                        Row {
                            Divider(
                                color = black,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(1.dp)
                            )
                            HorizontalSpacer(width = 20.dp)
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.clickable {
                                    scheduleViewModel.updateCheckEndTime(checkendTime.not())
                                    scheduleViewModel.updateCheckDate(false)
                                }
                            ) {
                                Text(
                                    text = "$selectYear.$selectMonth.$selectDay(${scheduleViewModel.selectDayOfWeek(selectYear,selectMonth,selectDay)})"
                                )
                                Text(
                                    text ="${selectEndTime.hours}:${selectEndTime.hours}"
                                )
                            }
                        }
                    }
                }

                AssistChip(
                    onClick = {
                        scheduleViewModel.updateCheckTime()
                        if (checkendTime && !checkTime)
                            scheduleViewModel.updateCheckDate(true)
                    },
                    label = {
                        Text(text = "시간선택")
                    }
                )

            }
            AnimatedVisibility(visible = scheduleViewModel.checkDate || checkendTime) {
                if (checkendTime){
                    DaysPicker(
                        timeVisibility = checkTime,
                        yearPick = selectYear,
                        monthPick = selectMonth,
                        dayPick = selectDay,
                        timePick = selectEndTime,
                        endDay = scheduleViewModel.endMonthDay,
                        onDayChange = {day -> scheduleViewModel.updateSelectDay(day)},
                        onMonthChange = {month -> scheduleViewModel.updateSelectMonth(month) },
                        onYearChange = {year -> scheduleViewModel.updateSelectYear(year)},
                        onTimeChange = {time -> scheduleViewModel.updateSelectEndTime(time)}
                    )
                }else{
                    DaysPicker(
                        timeVisibility = checkTime,
                        yearPick = selectYear,
                        monthPick = selectMonth,
                        dayPick = selectDay,
                        timePick = selectStartTime,
                        endDay = scheduleViewModel.endMonthDay,
                        onDayChange = {day -> scheduleViewModel.updateSelectDay(day)},
                        onMonthChange = {month -> scheduleViewModel.updateSelectMonth(month) },
                        onYearChange = {year -> scheduleViewModel.updateSelectYear(year)},
                        onTimeChange = {time -> scheduleViewModel.updateSelectStartTime(time)}
                    )
                }
            }
            Divider(
                color = gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(start = 15.dp, top = 10.dp, end = 15.dp)
                    .background(white, RoundedCornerShape(5.dp)),
                shape = RoundedCornerShape(5.dp),
                value = scheduleViewModel.inputCommentText,
                onValueChange = { scheduleViewModel.updateCommentText(it) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                maxLines = 3
            )

        }
    }
}

@Preview
@Composable
internal fun PreviewMainContainer() {
    WeekLayout(snackbarHostState = SnackbarHostState()) {
        Column() {
            VerticalSpacer(height = 10.dp)
            Divider(
                color = gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.5.dp)
            )
        }
    }
}