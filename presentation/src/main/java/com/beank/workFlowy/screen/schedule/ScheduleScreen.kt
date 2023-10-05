package com.beank.workFlowy.screen.schedule

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.beank.presentation.R
import com.beank.workFlowy.component.TextTopBar
import com.beank.workFlowy.component.TimeRangePickerDialog
import com.beank.workFlowy.component.ToggleCard
import com.beank.workFlowy.component.VerticalSpacer
import com.beank.workFlowy.component.WeekAppBar
import com.beank.workFlowy.component.WeekLayout
import com.beank.workFlowy.component.snackbar.SnackbarManager
import com.beank.workFlowy.ui.theme.gray
import com.beank.workFlowy.ui.theme.white
import com.beank.workFlowy.utils.toFormatShortString
import com.beank.workFlowy.utils.toFormatString
import com.beank.workFlowy.utils.toLocalDateTime
import com.beank.workFlowy.utils.toStartTimeLong
import com.beank.presentation.R.string as AppText


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleScreen(
    scheduleViewModel: ScheduleViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    onBackHome : () -> Unit
) {
    val alarmList = listOf("5분전","30분전","1시간전","3시간전","6시간전","12시간전","하루전")
    val resources = LocalContext.current.resources
    val uiState = scheduleViewModel.uiState
    val selectStartTime = uiState.startTime
    val selectEndTime = uiState.endTime
    val configuration = LocalConfiguration.current
    val startTimePickerState = rememberTimePickerState(
        initialHour = selectStartTime.hour,
        initialMinute = selectStartTime.minute,
        is24Hour = true
    )
    val endTimePickerState = rememberTimePickerState(
        initialHour = selectStartTime.hour,
        initialMinute = selectStartTime.minute,
        is24Hour = true
    )
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = uiState.date.toStartTimeLong(),
        yearRange = (2022..2025)
    )
    var showDateState by rememberSaveable { mutableStateOf(false) }
    var showTimeState by rememberSaveable { mutableStateOf(false) }
    var showPicker by remember { mutableStateOf(false) }
    var imageToggle by remember { mutableStateOf(false)}
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = Unit) {
        scheduleViewModel.initScheduleImages(resources.obtainTypedArray(R.array.scheduleList))
    }

    WeekLayout(
        snackbarHostState = snackbarHostState,
        topBar = {
            TextTopBar(
                title = if (uiState.id.isEmpty()) "일정 등록" else "일정 수정",
                onCancle = onBackHome,
                onConfirm = {
                    if (uiState.title.isNotEmpty()){
                        if (uiState.id.isNotEmpty())
                            scheduleViewModel.onScheduleUpdate()
                        else
                            scheduleViewModel.onScheduleInsert()
                        onBackHome()
                    }else{
                        SnackbarManager.showMessage(AppText.scheduleEmpty)
                    }
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
            OutlinedTextField(
                label = { Text(text = "일정") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(vertical = 10.dp, horizontal = 15.dp)
                    .background(white, RoundedCornerShape(5.dp)),
                shape = MaterialTheme.shapes.small,
                value = uiState.title,
                onValueChange = {
                    if (it.length <= 30)
                        scheduleViewModel.onTitleChange(it)
                    else
                        SnackbarManager.showMessage(AppText.max_length)
                    },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default),
                singleLine = true,
                maxLines = 1
            )
            Column(modifier = Modifier.verticalScroll(scrollState)) {
                OutlinedTextField(
                    label = { Text(text = "내용") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(vertical = 10.dp, horizontal = 15.dp)
                        .background(white, RoundedCornerShape(5.dp)),
                    shape = MaterialTheme.shapes.small,
                    value = uiState.comment,
                    onValueChange = {
                        if (it.length <= 150)
                            scheduleViewModel.onCommentChange(it)
                        else
                            SnackbarManager.showMessage(AppText.max_length)
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default)
                )

                ToggleCard(title = "알림 설정", checked = uiState.alarmToggle, height = 50.dp){
                    scheduleViewModel.onAlarmToggleChange()
                }
                AnimatedVisibility(visible = uiState.alarmToggle) {
                    LazyRow(modifier = Modifier.fillMaxWidth()){
                        items(alarmList){item ->
                            FilterChip(
                                modifier = Modifier.padding(horizontal = 5.dp),
                                selected = (item == uiState.alarmState),
                                onClick = { scheduleViewModel.onAlarmStateChange(item) },
                                label = {
                                    Text(text = item)
                                }
                            )
                        }
                    }
                }

                //날짜 시간 변경
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if (uiState.timeToggle) 115.dp else 85.dp)
                        .padding(10.dp),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 5.dp, horizontal = 15.dp)
                    ) {
                        Row {
                            Column(
                                verticalArrangement = Arrangement.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clickable { showDateState = true }
                                ){
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_calendar_today_24),
                                        contentDescription = "날짜 선택 아이콘",
                                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                        modifier = Modifier.size(50.dp)
                                    )
                                    Text(
                                        text = uiState.date.toFormatString(),
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        modifier = Modifier.padding(start = 15.dp)
                                    )
                                }
                                if(uiState.timeToggle) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .clickable { showTimeState = true }
                                    ){
                                        Icon(
                                            painter = painterResource(id = com.google.android.material.R.drawable.ic_clock_black_24dp),
                                            contentDescription = "시간 선택 아이콘",
                                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                            modifier = Modifier.size(40.dp)
                                        )
                                        Text(
                                            text = "${selectStartTime.hour}:${selectStartTime.minute} ~ ${selectEndTime.hour}:${selectEndTime.minute}",
                                            style = MaterialTheme.typography.headlineSmall,
                                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                                            modifier = Modifier.padding(start = 25.dp)
                                        )
                                    }



                                }
                            }
                        }
                        IconButton(onClick = { scheduleViewModel.onTimeToggleChange() }) {
                            Icon(
                                imageVector = if (uiState.timeToggle) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                                contentDescription = "시간 선택",
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
                //아이콘 변경
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if (imageToggle) 230.dp else 85.dp)
                        .padding(10.dp),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 5.dp, horizontal = 15.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { imageToggle = imageToggle.not() }
                        ) {
                            Row {
                                Icon(
                                    painter = painterResource(id = uiState.image),
                                    contentDescription = "스캐줄사진",
                                    modifier = Modifier
                                        .size(50.dp),
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Text(text = "아이콘 선택", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSecondaryContainer, modifier = Modifier.offset(x = 20.dp, y = 10.dp))
                            }
                            Icon(
                                imageVector = if (imageToggle) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                                contentDescription = "아이콘 선택",
                                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(top = 20.dp)
                            )
                        }
                        if(imageToggle) {
                            LazyVerticalGrid(
                                columns = GridCells.Adaptive(40.dp),
                                verticalArrangement = Arrangement.spacedBy(space = 5.dp),
                                horizontalArrangement = Arrangement.spacedBy(space = 5.dp),
                                contentPadding = PaddingValues(all = 5.dp)
                            ) {
                                items(uiState.scheduleImageList) { image ->
                                    Image(
                                        painter = painterResource(id = image),
                                        contentDescription = "스캐줄사진리스트",
                                        modifier = Modifier
                                            .height(40.dp)
                                            .clickable {
                                                scheduleViewModel.onImageChange(image)
                                            }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }


        //시간 다이얼로그
        if (showTimeState) {
            TimeRangePickerDialog(
                onCancel = { showTimeState = false },
                onConfirm = {
                    if(scheduleViewModel.onTimeChange(startTimePickerState.hour,startTimePickerState.minute,endTimePickerState.hour,endTimePickerState.minute)){
                        showTimeState = false
                    }else{
                        SnackbarManager.showMessage(AppText.time_error)
                    }
                },
                toggle = {
                    if (configuration.screenHeightDp > 400) {
                        IconButton(onClick = { showPicker = !showPicker }) {
                            val icon = if (showPicker) {
                                Icons.Outlined.KeyboardArrowUp
                            } else {
                                Icons.Outlined.DateRange
                            }
                            Icon(
                                icon,
                                contentDescription = if (showPicker) {
                                    "Switch to Text Input"
                                } else {
                                    "Switch to Touch Input"
                                }
                            )
                        }
                    }
                },
                startTimeContent = {
                    if (showPicker && configuration.screenHeightDp > 400) {
                        TimePicker(state = startTimePickerState)
                    } else {
                        TimeInput(state = startTimePickerState)
                    }
                },
                endTimeContent = {
                    if (showPicker && configuration.screenHeightDp > 400) {
                        TimePicker(state = endTimePickerState)
                    } else {
                        TimeInput(state = endTimePickerState)
                    }
                }
            )
        }
        //날짜 다이얼로그
        if (showDateState) {
            DatePickerDialog(
                onDismissRequest = { showDateState = false },
                confirmButton = {
                    TextButton(onClick = {
                        val date = datePickerState.selectedDateMillis?.toLocalDateTime()
                        date?.let {
                            scheduleViewModel.onDateChange(date.toLocalDate())
                        }
                        showDateState = false
                    }) {
                        Text(text = "확인")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDateState = false }) {
                        Text(text = "취소")
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState,
                    title = {
                        Text(
                            text = "날짜 선택",
                            modifier = Modifier.padding(top = 16.dp, start = 24.dp, end = 12.dp),
                            style = MaterialTheme.typography.headlineMedium
                        )
                    },
                    headline = {
                        Text(
                            text = datePickerState.selectedDateMillis?.toLocalDateTime()
                                ?.toLocalDate()?.toFormatShortString() ?: "",
                            modifier = Modifier.padding(start = 24.dp, end = 12.dp, bottom = 12.dp)
                        )
                    }
                )
            }

        }
    }
}

@Preview
@Composable
fun PreviewMainContainer() {
    WeekLayout(snackbarHostState = SnackbarHostState()) {
        Column() {
            VerticalSpacer(height = 10.dp)
            HorizontalDivider(
                thickness = 0.5.dp,
                color = gray
            )
        }
    }
}