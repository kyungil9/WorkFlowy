package com.example.workFlowy.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workFlowy.R
import com.example.workFlowy.WeekViewModel
import com.example.workFlowy.ui.theme.white
import com.example.workFlowy.utils.today
import com.example.workFlowy.utils.transDayToKorean

@Composable
fun WeekLayout(
    modifier: Modifier = Modifier,
    topBar : @Composable () -> Unit = {},
    weekViewModel: WeekViewModel,
    scaffoldState: ScaffoldState,
    content : @Composable (PaddingValues) -> Unit
){
    val selectDay by weekViewModel.selectDayFlow.collectAsState()
    var weekState by remember { mutableStateOf(false) }

    Scaffold (
        topBar = {topBar()},
        scaffoldState = scaffoldState,
        backgroundColor = white
    ){
        content(it)
        Column(modifier = Modifier.fillMaxSize()) {
            WeekLazyList(weekViewModel = weekViewModel)//주달력 표시
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text(text = "${selectDay.dayOfMonth}일 ${transDayToKorean(selectDay.dayOfWeek.value)}", textAlign = TextAlign.Start, fontSize = 24.sp, modifier = Modifier.padding(vertical = 12.dp,horizontal = 10.dp))
                Icon(painter = painterResource(id = R.drawable.baseline_wb_sunny_24), contentDescription = null, modifier = Modifier
                    .size(50.dp)
                    .padding(top = 10.dp, end = 15.dp))
            }
            ActCard(weekViewModel, onClickAct = {
                weekState = true
            })
            ScheduleList()
        }
        TagSelectedDialog(visible = weekState, weekViewModel = weekViewModel, onDismissRequest = {weekState = false}, onClickAct = {}, onAddActTag = {})
    }
}