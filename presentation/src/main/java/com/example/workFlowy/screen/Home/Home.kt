package com.example.workFlowy.screen.Home

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.workFlowy.R
import com.example.workFlowy.component.ActCard
import com.example.workFlowy.component.ScheduleList
import com.example.workFlowy.component.TagSelectedDialog
import com.example.workFlowy.component.WeekAppBar
import com.example.workFlowy.component.WeekLayout
import com.example.workFlowy.component.WeekLazyList
import com.example.workFlowy.navigation.NavigationItem
import com.example.workFlowy.screen.MainActivity
import com.example.workFlowy.utils.today
import com.example.workFlowy.utils.transDayToKorean
import java.time.LocalDate
import java.time.ZoneId


@Composable
fun HomeScreen(
    weekViewModel: WeekViewModel,
    onTailIconClick : () -> Unit,
    onAddTag : () -> Unit
){
    val selectDay by weekViewModel.selectDayFlow.collectAsStateWithLifecycle()
    val selectedTag by weekViewModel.selectedTagFlow.collectAsStateWithLifecycle()
    val uiState by weekViewModel.uiState.collectAsStateWithLifecycle()
    val progressTime by weekViewModel.progressTimeFlow.collectAsStateWithLifecycle()
    val selectDayString by weekViewModel.selectDayStringFlow.collectAsStateWithLifecycle(initialValue = "")
    var weekState by remember { mutableStateOf(false) }
    val dateDialog = DatePickerDialog(
        LocalContext.current,
        { _, year, month, day ->
            weekViewModel.changeSelectDay(LocalDate.of(year,month+1,day))
        },selectDay.year,
        selectDay.monthValue-1,
        selectDay.dayOfMonth
    )
    dateDialog.datePicker.minDate = LocalDate.of(2021,12,28).atTime(0,0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    dateDialog.datePicker.maxDate = LocalDate.of(2026,1,3).atTime(0,0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()


    WeekLayout(
        scaffoldState = rememberScaffoldState(),
        topBar = {
            WeekAppBar(
                headerIcon = R.drawable.baseline_dehaze_24,
                modifier = Modifier,
                selectDay = selectDayString,
                onContentClick = {
                    dateDialog.show()
                },
                tailIcon = NavigationItem.ANALYSIS.icon,
                onTailIconClick = onTailIconClick
            )
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            WeekLazyList(selectDay = selectDay, onClickItem = {weekViewModel.changeSelectDay(it)})//주달력 표시
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
            ScheduleList(uiState)
        }
        TagSelectedDialog(
            visible = weekState,
            uiState = uiState,
            onDismissRequest = {weekState = false},
            onClickAct = {tag ->
                weekViewModel.changeRecordInfo()
                weekViewModel.insertRecord(tag)
                weekState = false
            },
            onAddActTag = onAddTag
        )
    }
}