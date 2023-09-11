package com.example.workFlowy.screen.home

import android.app.DatePickerDialog
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.Schedule
import com.example.workFlowy.R
import com.example.workFlowy.component.ActCard
import com.example.workFlowy.component.ScheduleList
import com.example.workFlowy.component.TagSelectedDialog
import com.example.workFlowy.component.WeekAppBar
import com.example.workFlowy.component.WeekBottomBar
import com.example.workFlowy.component.WeekLayout
import com.example.workFlowy.component.WeekLazyList
import com.example.workFlowy.navigation.NavigationItem
import com.example.workFlowy.utils.transDayToKorean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import kotlin.coroutines.coroutineContext


@Composable
fun HomeScreen(
    weekViewModel: WeekViewModel,
    onTailIconClick : () -> Unit,
    onMoveMisson : () -> Unit,
    onMoveSchedule : () -> Unit,
    onMoveTag : () -> Unit
){
    val scope = rememberCoroutineScope()
    val selectDay by weekViewModel.selectDayFlow.collectAsStateWithLifecycle()
    val selectedTag by weekViewModel.selectedTagFlow.collectAsStateWithLifecycle()
    val uiState by weekViewModel.uiState.collectAsStateWithLifecycle()
    val progressTime by weekViewModel.progressTimeFlow.collectAsStateWithLifecycle()
    val selectDayString by weekViewModel.selectDayStringFlow.collectAsStateWithLifecycle(initialValue = "")
    val listCenter by remember { mutableStateOf(ChronoUnit.DAYS.between(LocalDate.of(2021,12,28),LocalDate.now()).toInt() -3)}
    val weekListState = rememberLazyListState(initialFirstVisibleItemIndex = listCenter)
    var weekState by remember { mutableStateOf(false) }
    var scheduleState by remember { mutableStateOf(false) }
    var scheduleInfo by remember { mutableStateOf(Schedule(null, LocalDate.now(), LocalTime.now(),
        LocalTime.now(),0,"",""))}
    var snackbarHostState = remember { SnackbarHostState() }
    val dateDialog = DatePickerDialog(
        LocalContext.current,
        { _, year, month, day ->
            weekViewModel.changeSelectDay(LocalDate.of(year,month+1,day))
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
                onTailIconClick = onTailIconClick
            )
        },
        bottomBar = {
            WeekBottomBar(
                checked = scheduleState,
                onMoveMisson = onMoveMisson,
                onMoveToday = {
                    scope.launch(Dispatchers.Default) {
                        if (!selectDay.isEqual(LocalDate.now())){
                            weekViewModel.changeSelectDay(LocalDate.now())
                            weekListState.animateScrollToItem(listCenter)
                        }
                    }
                },
                onCheckSchedule = { /*TODO*/ },
                onDeleteSchedule = {
                    weekViewModel.deleteSchedule(scheduleInfo)
                    scheduleState = scheduleState.not()},
                onUpdateSchedule = { /*TODO*/ },
                onAdditionalSchedule = { onMoveSchedule() }
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
                onClickItem = {
                    scope.launch(Dispatchers.Default) {
                        weekListState.animateScrollToItem(ChronoUnit.DAYS.between(LocalDate.of(2021,12,28),it).toInt()-3)
                    }
                    weekViewModel.changeSelectDay(it)
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
            ActCard(selectedTag = selectedTag,
                progressTime = progressTime,
                onClickAct = {
                    weekState = true
                })
            ScheduleList(
                uiState = uiState,
                onClickSchedule = {schedule ->
                    if (scheduleInfo === schedule) {
                        scheduleState = scheduleState.not()
                    }else{
                        scheduleInfo = schedule
                        scheduleState = true
                    }
                })
        }
        TagSelectedDialog(
            visible = weekState,
            uiState = uiState,
            onDismissRequest = {weekState = false},
            onClickAct = {tag ->
                weekViewModel.changeRecordInfo()
                weekState = false
                weekViewModel.insertRecord(tag)
            },
            onAddActTag = {
                weekState = false
                onMoveTag()},
            onClickDelect = {weekViewModel.deleteSelectTag(it)}
        )
    }
}