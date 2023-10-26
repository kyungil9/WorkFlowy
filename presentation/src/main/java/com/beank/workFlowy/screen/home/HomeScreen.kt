package com.beank.workFlowy.screen.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.beank.domain.model.Schedule
import com.beank.presentation.R
import com.beank.workFlowy.component.ActCard
import com.beank.workFlowy.component.ScheduleList
import com.beank.workFlowy.component.TagSelectedDialog
import com.beank.workFlowy.component.WeekAppBar
import com.beank.workFlowy.component.WeekBottomBar
import com.beank.workFlowy.component.WeekLayout
import com.beank.workFlowy.component.WeekLazyList
import com.beank.workFlowy.navigation.NavigationItem
import com.beank.workFlowy.utils.toFormatShortString
import com.beank.workFlowy.utils.toLocalDateTime
import com.beank.workFlowy.utils.toStartTimeLong
import com.beank.workFlowy.utils.transDayToKorean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.temporal.ChronoUnit


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    weekViewModel: WeekViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    openScreen: (String) -> Unit,
    openSchedule: (String,LocalDate) -> Unit,
    openEditSchedule: (String,Schedule) -> Unit
){
    val scope = rememberCoroutineScope()
    val selectDay by weekViewModel.selectDayFlow.collectAsStateWithLifecycle()
    val selectDayString by weekViewModel.selectDayStringFlow.collectAsStateWithLifecycle()
    val uiState = weekViewModel.uiState
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectDay.toStartTimeLong(),
        yearRange = (2022..2025)
    )
    val weekListState = rememberLazyListState(initialFirstVisibleItemIndex = uiState.listCenter)
    var dateDialogState by rememberSaveable { mutableStateOf(false)}
    val onWeekListClick = remember<(LocalDate) -> Unit> {
        {date ->
            if (!date.isEqual(selectDay)) {
                scope.launch(Dispatchers.Main) {
                    weekListState.animateScrollToItem(ChronoUnit.DAYS.between(LocalDate.of(2021, 12, 28), date).toInt() - 3)
                }
                weekViewModel.onSelectDayChange(date)
                weekViewModel.onScheduleStateChange(false)
            }
        }
    }


    LaunchedEffect(key1 = Unit){
        withContext(Dispatchers.IO){
            weekViewModel.timerJob.start()
        }
    }

    WeekLayout(
        snackbarHostState = snackbarHostState,
        topBar = remember{{
            WeekAppBar(
                headerIcon = R.drawable.baseline_settings_24,
                onHeaderIconClick = {openScreen(NavigationItem.SETTING.route) },
                selectDay = {selectDayString},
                onContentClick ={
                    dateDialogState = true
                },
                tailIcon = NavigationItem.ANALYSIS.icon,
                onTailIconClick = {
                    weekViewModel.onRecordReduce()
                    openScreen(NavigationItem.ANALYSIS.route)
                }
            )
        }},
        bottomBar = remember{{
            WeekBottomBar(
                checked = {uiState.scheduleState},
                onMoveMisson = { openScreen(NavigationItem.MISSON.route) },
                onMoveToday = {
                    scope.launch(Dispatchers.Default) {
                        if (!selectDay.isEqual(LocalDate.now())){
                            weekViewModel.onSelectDayChange(LocalDate.now())
                            weekListState.animateScrollToItem(uiState.listCenter)
                        }
                    }
                },
                onCheckSchedule = weekViewModel::onCheckScheduleChange,
                onDeleteSchedule = {
                    weekViewModel.onScheduleDelete()
                    weekViewModel.onScheduleStateChange(false)},
                onUpdateSchedule = {
                    openEditSchedule(NavigationItem.SCHEDULE.route,uiState.selectSchedule)
                    weekViewModel.onScheduleStateChange(false)},
                onAdditionalSchedule = { openSchedule(NavigationItem.SCHEDULE.route,selectDay)}
            )
        }}
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding(), bottom = it.calculateBottomPadding())
        ) {
            WeekLazyList(
                weekListState = weekListState,
                onClickItem = onWeekListClick,
                weekDayList = {uiState.weekDayList}
            )//주달력 표시
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text(text = "${selectDay.dayOfMonth}일 ${transDayToKorean(selectDay.dayOfWeek.value)}",
                    textAlign = TextAlign.Start,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(vertical = 12.dp,horizontal = 10.dp))
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_wb_sunny_24),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .padding(top = 10.dp, end = 15.dp))
            }

            ActCard(selectedTag = {uiState.selectTag},
                progressTime = {uiState.progressTime},
                progress = {uiState.actProgress},
                onClickAct = remember{{
                    weekViewModel.onWeekStateChange(true)
                }})

            ScheduleList(
                scheduleList = {uiState.scheduleList},
                onRightDrag = remember{{
                    val date = weekViewModel.plusSelectDay()
                    scope.launch(Dispatchers.Main) {
                        weekListState.animateScrollToItem(ChronoUnit.DAYS.between(LocalDate.of(2021, 12, 28), date).toInt() - 3)
                    }}},
                onLeftDrag = remember{{
                    val date = weekViewModel.minusSelectDay()
                    scope.launch(Dispatchers.Main) {
                        weekListState.animateScrollToItem(ChronoUnit.DAYS.between(LocalDate.of(2021, 12, 28), date).toInt() - 3)
                    }}},
                onClickSchedule = remember{{schedule ->
                    if (uiState.selectSchedule == schedule) {
                        if (uiState.scheduleState)
                            weekViewModel.onScheduleStateChange(false)
                        else
                            weekViewModel.onScheduleStateChange(true)
                    }else{
                        weekViewModel.setSelectScheduleInfo(schedule)
                        weekViewModel.onScheduleStateChange(true)
                    }
                }})
        }
        TagSelectedDialog(
            visible = {uiState.weekState},
            tagList = {uiState.tagList},
            onDismissRequest = {weekViewModel.onWeekStateChange(false)},
            onClickAct = {tag ->
                weekViewModel.onRecordChange(tag)
                weekViewModel.onWeekStateChange(false)
            },
            onAddActTag = {
                weekViewModel.onWeekStateChange(false)
                openScreen(NavigationItem.TAG.route)},
            onClickDelect = weekViewModel::onTagDelete
        )
        if (dateDialogState){
            DatePickerDialog(
                onDismissRequest = { dateDialogState = false },
                confirmButton = {
                    TextButton(onClick = {
                        val date = datePickerState.selectedDateMillis?.toLocalDateTime()
                        date?.let {
                            weekViewModel.onSelectDayChange(date.toLocalDate())
                            scope.launch (Dispatchers.Main) {
                                weekListState.scrollToItem(ChronoUnit.DAYS.between(LocalDate.of(2021,12,28),date.toLocalDate()).toInt()-3)
                            }
                        }
                        dateDialogState = false
                    }) {
                        Text(text = "확인")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { dateDialogState = false }) {
                        Text(text = "취소")
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState,
                    title = {
                        Text(text = "날짜 선택", modifier = Modifier.padding(top = 16.dp, start = 24.dp, end = 12.dp),style = MaterialTheme.typography.headlineMedium)
                    },
                    headline = {
                        Text(
                            text = datePickerState.selectedDateMillis?.toLocalDateTime()?.toLocalDate()?.toFormatShortString() ?: "",
                            modifier = Modifier.padding(start = 24.dp, end = 12.dp, bottom = 12.dp)
                        )
                    }
                )
            }
        }
        
    }
}