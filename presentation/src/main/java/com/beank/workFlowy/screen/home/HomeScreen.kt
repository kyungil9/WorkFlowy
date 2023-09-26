package com.beank.workFlowy.screen.home

import android.app.DatePickerDialog
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.beank.workFlowy.utils.transDayToKorean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    weekViewModel: WeekViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    openScreen: (String) -> Unit,
    openSchedule: (String,LocalDate) -> Unit,
    openEditSchedule: (String,Schedule) -> Unit
){
    weekViewModel.timerJob.start()
    val scope = rememberCoroutineScope()
    val selectDay by weekViewModel.selectDayFlow.collectAsStateWithLifecycle()
    val selectDayString by weekViewModel.selectDayStringFlow.collectAsStateWithLifecycle()
    val uiState = weekViewModel.uiState
    val weekListState = rememberLazyListState(initialFirstVisibleItemIndex = uiState.listCenter)

    val dateDialog = DatePickerDialog(
        LocalContext.current,
        { _, year, month, day ->
            weekViewModel.onSelectDayChange(LocalDate.of(year,month+1,day))
            scope.launch (Dispatchers.Default) {
                weekListState.scrollToItem(ChronoUnit.DAYS.between(LocalDate.of(2021,12,28),LocalDate.of(year,month+1,day)).toInt()-3)
            }
        },selectDay.year,
        selectDay.monthValue-1,
        selectDay.dayOfMonth
    )
    dateDialog.datePicker.minDate = LocalDate.of(2021,12,28).atTime(0,0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    dateDialog.datePicker.maxDate = LocalDate.of(2026,1,3).atTime(0,0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

    WeekLayout(
        snackbarHostState = snackbarHostState,
        topBar = {
            WeekAppBar(
                headerIcon = R.drawable.baseline_dehaze_24,
                selectDay = selectDayString,
                onContentClick = {
                    dateDialog.show()
                },
                tailIcon = NavigationItem.ANALYSIS.icon,
                onTailIconClick = {openScreen(NavigationItem.ANALYSIS.route)}
            )
        },
        bottomBar = {
            WeekBottomBar(
                checked = uiState.scheduleState,
                onMoveMisson = { openScreen(NavigationItem.MISSON.route)},
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
                onUpdateSchedule = { openEditSchedule(NavigationItem.SCHEDULE.route,uiState.selectSchedule) },
                onAdditionalSchedule = { openSchedule(NavigationItem.SCHEDULE.route,selectDay) }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding(), bottom = it.calculateBottomPadding())
        ) {
            WeekLazyList(
                selectDay = selectDay,
                weekListState = weekListState,
                onClickItem = {date ->
                    if (!date.isEqual(selectDay)) {
                        scope.launch(Dispatchers.Default) {
                            weekListState.animateScrollToItem(ChronoUnit.DAYS.between(LocalDate.of(2021, 12, 28), date).toInt() - 3)
                        }
                        weekViewModel.onSelectDayChange(date)
                        weekViewModel.onScheduleStateChange(false)
                    }
                }
            )//주달력 표시
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text(text = "${selectDay.dayOfMonth}일 ${transDayToKorean(selectDay.dayOfWeek.value)}",
                    textAlign = TextAlign.Start,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(vertical = 12.dp,horizontal = 10.dp))
                Icon(
                    painter = painterResource(id = R.drawable.baseline_wb_sunny_24),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .padding(top = 10.dp, end = 15.dp))
            }

            ActCard(selectedTag = uiState.selectTag,
                progressTime = uiState.progressTime,
                progress = uiState.actProgress,
                onClickAct = {
                    weekViewModel.onWeekStateChange(true)
                })

            ScheduleList(
                uiState = uiState,
                onRightDrag = {
                    val date = weekViewModel.plusSelectDay()
                    scope.launch(Dispatchers.Main) {
                        weekListState.animateScrollToItem(ChronoUnit.DAYS.between(LocalDate.of(2021, 12, 28), date).toInt() - 3)
                    }},
                onLeftDrag = {
                    val date = weekViewModel.minusSelectDay()
                    scope.launch(Dispatchers.Main) {
                        weekListState.animateScrollToItem(ChronoUnit.DAYS.between(LocalDate.of(2021, 12, 28), date).toInt() - 3)
                    }},
                onClickSchedule = {schedule ->
                    if (uiState.selectSchedule == schedule) {
                        if (uiState.scheduleState)
                            weekViewModel.onScheduleStateChange(false)
                        else
                            weekViewModel.onScheduleStateChange(true)
                    }else{
                        weekViewModel.setSelectScheduleInfo(schedule)
                        weekViewModel.onScheduleStateChange(true)
                    }
                })
        }
        TagSelectedDialog(
            visible = uiState.weekState,
            uiState = uiState,
            onDismissRequest = {weekViewModel.onWeekStateChange(false)},
            onClickAct = {tag ->
                weekViewModel.onRecordChange(tag)
                weekViewModel.onWeekStateChange(false)
            },
            onAddActTag = {
                weekViewModel.onWeekStateChange(false)
                openScreen(NavigationItem.TAG.route)},
            onClickDelect = {weekViewModel.onTagDelete(it)}
        )
    }
}